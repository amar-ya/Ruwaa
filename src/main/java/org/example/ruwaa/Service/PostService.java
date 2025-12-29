package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.DTOs.LearningContentDTO;
import org.example.ruwaa.DTOs.WorkPostDTO;
import org.example.ruwaa.Model.*;
import org.example.ruwaa.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService
{
    private final CategoriesRepository categoriesRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;
    private final AiService aiService;
    private final ExpertRepository expertRepository;
    private final AttachmentsRepository attachmentsRepository;


    public List<Post> getAll(){
        List<Post> postList = postRepository.findAll();
        if (postList.isEmpty()){
            throw new ApiException("no posts found");
        }
        return postList;
    }



    public void addWorkPost(String username,MultipartFile attachement, String content, Boolean isPublic, String postCategory) throws IOException {
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        if(!u.getRole().equals("CUSTOMER")) throw new ApiException("only customers can add this content type");
        Categories category = categoriesRepository.findCategoryByName(postCategory).orElseThrow(() -> new ApiException("invalid category"));

        String type = (isPublic) ? "public_work":"private_work";
        HashSet<Users> permitPrivateWorkVisiablity;
        if(isPublic) permitPrivateWorkVisiablity=null;
        else {
            permitPrivateWorkVisiablity= new HashSet<Users>();
            permitPrivateWorkVisiablity.add(u); //adding the current user so they can see their own work :)
        }

        Post p = new Post();
        p.setPublishAt(LocalDateTime.now());
        p.setCategory(category);
        p.setUsers(u);
        p.setContent(content);
        p.setType(type);
        p.setViews(0);
        p.setPermitWorkVisiablity(permitPrivateWorkVisiablity);
        postRepository.save(p);
        Attachments a = new Attachments();
        a.setPost(p);
        a.setData(attachement.getBytes());
        a.setName(attachement.getOriginalFilename());
        a.setType(attachement.getContentType());
        attachmentsRepository.save(a);
    }


    public void addLearningContent(String username,MultipartFile image,String content, Boolean isFree) throws IOException {
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        if(!u.getRole().equals("EXPERT")) throw new ApiException("only expert can add this content type");
        Expert e = expertRepository.findExpertById(u.getId()).orElseThrow();
        if(!e.getIsActive()) throw new ApiException("expert account not activated yet");
        String type = (isFree) ? "free_content":"subscription_content";


        Post post = new Post();
        post.setPublishAt(LocalDateTime.now());
        post.setUsers(u);
        post.setContent(content);
        post.setType(type);
        post.setViews(0);
        post.setCategory(u.getExpert().getCategory());
        postRepository.save(post);
        Attachments a = new Attachments();
        a.setPost(post);
        a.setData(image.getBytes());
        a.setName(image.getOriginalFilename());
        a.setType(image.getContentType());
        attachmentsRepository.save(a);



    }

    public void updateWorkPost(String username, Integer id, WorkPostDTO dto){
        Users u  = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));
        if (u != post.getUsers()){
            throw new ApiException("only post author can update it");
        }
        post.setContent(dto.getContent());
        post.setAttachment(dto.getAttachments());

        postRepository.save(post);
    }
    public void updateLearningCont(String username, Integer id, LearningContentDTO dto)  {
        Users u  = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));
        if (u != post.getUsers()){
            throw new ApiException("only post author can update it");
        }
        post.setContent(dto.getContent());
        post.setAttachment(dto.getAttachments());

        postRepository.save(post);
    }

    public void deletePost(String username, Integer id){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post m = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));
        if (u != m.getUsers()){
            throw new ApiException("only post author can remove it");
        }
        postRepository.delete(m);
    }


    public Post viewWorkPost(String username ,Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
    if (p.getType().contains("content")){
        throw new ApiException("post was not found");
    }
    if(p.getType().equals("private_work"))
     if(!p.getPermitWorkVisiablity().contains(user)) throw new ApiException("This is private work");
    p.setViews(p.getViews()+1);
    postRepository.save(p);
    return p;
    }

    public Post viewLearningPost(String username ,Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        if (p.getType().contains("work")){
            throw new ApiException("post was not found");
        }
        if(p.getType().equals("subscription_content")) {

            if (user.getRole().equals("CUSTOMER")) {
                Customer customer = user.getCustomer();
                if (customer.getSubscription() == null || customer.getSubscription().getEnd_date().isBefore(LocalDateTime.now()))
                    throw new ApiException("this content needs subscription");

            }
            else if(!p.getUsers().equals(user)&&user.getRole().equals("EXPERT")) throw new ApiException("this content for customer");

        } //sub

        p.setViews(p.getViews()+1);
        postRepository.save(p);
        return p;
    }


    public List<Post> freeFeed(){
        return postRepository.findPostByType("free_content");
    }


    public List<Post> subscribeFeed(String username){
        Users users = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));
        if(users.getRole().equals("EXPERT")) throw new ApiException("subscription content feed allowed for customers only");
        if(!users.getRole().equals("ADMIN")){
            Customer customer = users.getCustomer();
            if (customer.getSubscription() == null || customer.getSubscription().getEnd_date().isBefore(LocalDateTime.now()))
                throw new ApiException("this feed needs subscription");
        }
        return postRepository.findPostByType("subscription_content");
    }


    public List<Post> workFeed(){
        return postRepository.findPostByType("public_work");
    }





    public List<Post> getMyPost(String username){
        Users user = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("Account not found"));
        return postRepository.findPostByUsers_id(user.getId());
    }


    public String reviewMyWork(String username, Integer postId){
        Users u =  usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if (u != p.getUsers()){
            throw new ApiException("only post author can review it using AI");
        }
        if (u.getCustomer().getSubscription() == null || u.getCustomer().getSubscription().getEnd_date().isBefore(LocalDateTime.now()) && u.getExpert() == null){
            throw new ApiException("this content needs subscription");
        }
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");


        String dtoString = " بناءًا على العمل المعطى (قد يكون كود برمجي، عمل فني، الخ) ابيك تقييم هذا العمل بمعايير قياسية فعلية، وتعطيني الاجابة مباشرة وبدون ايموجي التقييم من 5، الأسلوب والهوية الفنية (Style & Originality)" +
                "الأسلوب والهوية الفنية (Style & Originality)" +
                " ما حققته:" +
                "أسلوب شاعري واضح." +
                "حس تعبيري أقرب للفن المعاصر." +
                "العمل يبدو “صوتًا واحدًا” لا تجميع تقنيات." +
                "ما خالفته / يمكن تحسينه:" +
                "الأسلوب قريب من مدارس معروفة؛ يحتاج توقيعًا شخصيًا أقوى." +
                "التقييم:  (4/5)"
                ;


             dtoString += aiService.dtoPost(p);

        return  aiService.askAI(dtoString);
    }

    public void changeVisibilityToPublic(String username, Integer postId){
        Users u =  usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if (u != p.getUsers()){
            throw new ApiException("only the post`s author can change visibility");
        }
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if(p.getType().equals("public_work")) throw new ApiException("this work is already Public");

        p.setType("public_work");
        postRepository.save(p);

    }


    public void changeVisibilityToPrivate(String username, Integer postId){
        Users u =  usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if(p.getType().equals("private_work")) throw new ApiException("this work is already Private");
        if (u != p.getUsers()){
            throw new ApiException("only the post`s author can change visibility");
        }
        p.setType("private_work");
        postRepository.save(p);

    }



}
