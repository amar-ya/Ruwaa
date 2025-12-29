package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.DTOs.ReviewDTO;
import org.example.ruwaa.Model.*;
import org.example.ruwaa.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private final ChatRepository chatRepository;

    private final ReviewRepository reviewRepository;
    private final PostRepository mediaRepository;
    private final ExpertRepository expertRepository;
    private final PostRepository postRepository;
    private final PaymentService paymentService;
    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;
    private final SendMailService sendMailService;
    private final AiService aiService;


    public List<Review> getAll(){
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()){
            throw new ApiException("no reviews found");
        }
        return reviews;
    }

//    public void add(Integer expert_id,Integer media_id,Review review){
//        Expert e = expertRepository.findExpertById(expert_id).orElseThrow(() -> new ApiException("expert not found"));
//
//        Post m = mediaRepository.findPostById(media_id).orElseThrow(() -> new ApiException("post not found"));
//        review.setRate(1);
//        review.setHasRated(false);
//        review.setPost(m);
//        review.setExpert(e);
//        reviewRepository.save(review);
//    }

    public void update(Integer id, Review review){
        Review r = reviewRepository.findReviewById(id).orElseThrow(() -> new ApiException("review not found"));

        r.setContent(review.getContent());
        reviewRepository.save(r);
    }

    public void delete(Integer id){
        Review r = reviewRepository.findReviewById(id).orElseThrow(() -> new ApiException("review not found"));

        reviewRepository.delete(r);
    }

    public List<Review> getFinishedReviews(){
        List<Review> r = reviewRepository.findFinishedReviews();
        if (r.isEmpty()){
            throw new ApiException("there are no finished reviews");
        }
        return r;
    }

    public List<Review> getUnfinishedReviews(){
        List<Review> r = reviewRepository.findUnfinishedReviews();
        if (r.isEmpty()){
            throw new ApiException("there are no unfinished reviews");
        }
        return r;
    }

//    public void requestReview(Integer postId, Integer expertId) {
//
//        Post post = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
//
//        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("expert not found"));
//
//
//        Review review = new Review();
//        review.setStatus("Pending");
//        review.setExpert(expert);
//        review.setPost(post);
//        reviewRepository.save(review);
//    }



    public void rateReview(Integer reviewId,Integer rate){
        Review review = reviewRepository.findReviewById(reviewId).orElseThrow(()-> new ApiException("review not found"));
        if(review.getHasRated()) throw new ApiException("you have rated this review before");
        if(rate>5||rate<1) throw new ApiException("invalid rate number");
        review.setHasRated(true);
        review.setRate(rate);
        reviewRepository.save(review);

        Expert expert = review.getExpert();

        //I need variable names
    expert.setTotal_rating(expert.getTotal_rating()+rate);
    expert.setCount_rating(expert.getCount_rating()+1);
//    expert.setReviewRate(expert.getSUM()/expert.getCounter());

        expertRepository.save(expert);

    }


    public void requestReview(Integer expert_id,Integer workId){
        Expert e = expertRepository.findExpertById(expert_id).orElseThrow(() -> new ApiException("expert not found"));

        Post p = mediaRepository.findPostById(workId).orElseThrow(() -> new ApiException("work not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if (p.getUsers().getCards() == null){
            throw new ApiException("add a card first to pay");
        }
        Subscription s = p.getUsers().getCustomer().getSubscription();
        if(s == null || s.getEnd_date().isBefore(LocalDateTime.now())){
            Double amount = e.getConsult_price()+(e.getConsult_price()*0.2);
            paymentService.processPayment(amount,p.getUsers().getCards().get(0));
        }else {
            Double amount = e.getConsult_price()+(e.getConsult_price()*0.1);
            paymentService.processPayment(amount,p.getUsers().getCards().get(0));
        }
        Review review = new Review();
        review.setStatus("Pending"); ///****************Check regex
        review.setRate(1);
        review.setHasRated(false);
        review.setPost(p);
        review.setExpert(e);
        e.getReviews().add(review);
        expertRepository.save(e);
        reviewRepository.save(review);
        //notify expert         **update link later for better UX
        String body = "a new review request worth "+e.getConsult_price()+"SR,\n\n" + //**** كم نخلي نسبة الربح حقتنا عشان نطرح
                "View request link: \n\n" +
                "Work content: "+p.getContent();
        sendMailService.sendMessage(e.getUsers().getEmail(),"New Review Request From "+p.getUsers().getName(),body);
    }


    public void submitReview(Integer reviewId,String username ,ReviewDTO reviewDTO){
        Review review = reviewRepository.findReviewById(reviewId).orElseThrow(()-> new ApiException("review not found"));
        Users expert = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("expert not found"));
        if(!expert.getRole().equals("EXPERT")) throw new ApiException("unAuthorized writing review");
        Expert expert1 = expertRepository.findExpertByUsername(username).orElseThrow(() -> new ApiException("expert not found"));

        //
        // if(!review.getStatus().equals("Accepted")) throw new ApiException("you need to accept review first");

        review.setContent(reviewDTO.getContent());
        review.setStatus("Completed"); //***********CHECK REGEX


//        Chat chat = new Chat(null,true,new ArrayList<>(),review);
//        review.setChat(chat);
//        chatRepository.save(chat);
        reviewRepository.save(review);

        Double credit = expert.getExpert().getConsult_price();

        expert.setBalance(expert.getBalance()+credit);

        expert.getExpert().setReview_count(expert1.getReview_count()+1);
        expertRepository.save(expert.getExpert());
        String message = "your review request to: "+expert.getName()+"\n saying: "+reviewDTO.getContent();
        sendMailService.sendMessage(review.getPost().getUsers().getEmail(),"review submitted",message);
    }


    public void acceptReview ( String username, Integer reviewId) {
        Users expert = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        Review review = reviewRepository.findReviewById(reviewId).orElseThrow(() -> new ApiException("review not found"));

        if (!expert.getId().equals(review.getExpert().getId())) {
            throw new ApiException("This review doesn't belong to you");
        }
        review.setStatus("Accepted");
        reviewRepository.save(review);
    }


    public void rejectReview (String username, Integer reviewId,String reason) {
        Users expert = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        Review review = reviewRepository.findReviewById(reviewId).orElseThrow(() -> new ApiException("review not found"));
        System.out.println(review.getExpert());

        if (!expert.getId().equals(review.getExpert().getId())) {
            throw new ApiException("This review doesn't belong to you");
        }

        String message = "the request to review you post\n"+review.getPost().getContent()+ " \n were rejected by :"+expert.getName()+" for the following reason: "+reason;
        sendMailService.sendMessage(review.getPost().getUsers().getEmail(),"review rejected",message);
        review.setStatus("Rejected");
        reviewRepository.save(review);
    }


    public void rejectAll (String username) {
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        Expert expert = expertRepository.findExpertById(user.getId()).orElseThrow(()-> new ApiException("expert not found"));

        List<Review> reviews = reviewRepository.findAllByExpert(expert);

        for (Review review : reviews) {
            if (review.getStatus().equals("Pending")) {
                review.setStatus("Rejected");
            }
        }
        reviewRepository.saveAll(reviews);
    }


    public List<Review> getPendingReviews (String username) {
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        Expert expert = expertRepository.findExpertById(user.getId()).orElseThrow(()-> new ApiException("expert not found"));

        List<Review> reviews = reviewRepository.findPendingReviews(expert);

        if (reviews.isEmpty()) {
            throw new ApiException("No pending reviews found");
        }
        return reviews;
    }


    public List<Review> getReviewsRequest (String username) {
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        Expert expert = expertRepository.findExpertById(user.getId()).orElseThrow(() -> new ApiException("Expert not found"));

        List<Review> reviews = reviewRepository.findAllByExpert(expert);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return reviews;
    }


    public List<Review> getSentRequests(String username) {
        Users customer = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));

        List<Review> customerRequests = new ArrayList<>();
        List<Review> all = reviewRepository.findAll();

        for (Review review : all) {
            if (review.getPost().getUsers().getId().equals(customer.getId())){
                customerRequests.add(review);
            }
        }
        return customerRequests;
    }

    public List<Review> getCompletedReviewsByPost(Integer postId, String username) {
        Customer c = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new ApiException("Customer not found"));
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("Post not found"));
        if (p.getUsers() != c.getUsers()){
            throw new ApiException("this post doesnt belong to you");
        }
        List<Review>  completed = reviewRepository.findCompletedReviewsOfPost(postId);

        if (completed.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return completed;
    }

    public String makeReviewTemplate(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");

        //AI
        String work = aiService.dtoPost(p);
        return  aiService.askAI("بناءًا على العمل المعطى (قد يكون كود برمجي، عمل فني، الخ)\n" +
                "ابيك تجيب المعايير القياسية الفعلية\n" +
                "بشكل مباشر مثلا:\n" +
                "المعايير الأساسية لتقييم الرسمة الرقمية الفنية\n" +
                "1.  المنظور (Perspective)\n" +
                "2.  التشريح والنِسَب (Anatomy & Proportion) (للشخصيات)\n" +
                "الخ..\n" +
                "\n" +
                "المهم ابيك تعطينياها مباشرة وبدون ايموجي\n" +
                "\n" +
                "\n"+work);
    }

    public String reviewAssistance(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");

        //AI

        String work = aiService.dtoPost(p);
        return aiService.askAI("بناءًا على العمل المعطى (قد يكون كود برمجي، عمل فني، الخ)\n" +
                "ابيك تجيب المعايير القياسية الفعلية\n" +
                "بشكل مباشر مع ذكر الاسئلة والادوات اللتي قد تساعد، مثلا: \n" +
                "المعايير الأساسية لتقييم الرسمة الرقمية الفنية\n" +
                "1. التكوين (Composition) \n" +
                "\n" +
                "\n" +
                " هل عين المشاهد تُقاد بذكاء؟\n" +
                " هل هناك نقطة تركيز واضحة؟\n" +
                " هل الفراغ مستغل جيدًا؟\n" +
                "\n" +
                "أدوات تقييم:\n" +
                "\n" +
                "Rule of thirds\n" +
                "Balance\n" +
                "Visual hierarchy\n" +
                "Negative space\n" +
                "\n" +
                "\n" +
                "عطني الاجابة فقط \n" +
                " مباشرة وبدون ايموجي \n" +
                " "+work);
    }





}

