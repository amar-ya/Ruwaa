package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.PostRepository;
import org.example.ruwaa.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExpertService
{
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExpertRepository expertRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    public List<Expert> getAll(){
        List<Expert> experts = expertRepository.findAll();
        if (experts.isEmpty()){
            throw new ApiException("expert not found");
        }
        return experts;
    }

    public void update(Integer id, Users user){
        Expert e = expertRepository.findExpertById(id).orElseThrow(() -> new ApiException("expert not found"));

        Users u = e.getUsers();
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setPhone(user.getPhone());
        u.setName(user.getName());
        e.setUsers(u);
        expertRepository.save(e);
    }

    public void delete(Integer id){
        Expert e = expertRepository.findExpertById(id).orElseThrow(() -> new ApiException("expert not found"));

        expertRepository.delete(e);
    }

    public Expert findMostActiveExpertByCategory(String category){
        return expertRepository.findMostActiveExpert(category).orElseThrow(() -> new ApiException("no experts added to this category"));
    }


    public List<Expert> getExpertByCategory (String category) {
        List<Expert> experts = expertRepository.findExpertByCategory(category);
        for (Expert e : experts) {
            e.setConsult_price(e.getConsult_price()+(e.getConsult_price()*0.2));
        }
        if (experts.isEmpty()) {
            throw new ApiException("No experts found");
        }

        return experts;
    }



    public void applyDiscount(Integer expertId, Double discountPercentage, LocalDate date) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new ApiException("Expert not found"));
        if(date.isBefore(LocalDate.now())) throw new ApiException("invalid date");

        Double originalPrice = expert.getConsult_price();

        double newPrice = originalPrice * (1 - discountPercentage / 100);
        expert.setConsult_price(newPrice);
        expertRepository.save(expert);


        long days = ChronoUnit.DAYS.between(LocalDate.now(), date);


        scheduler.schedule(() -> {
            Expert e = expertRepository.findById(expertId).orElse(null);
            if (e != null) {
                e.setConsult_price(originalPrice);
                expertRepository.save(e);
            }
        }, days, TimeUnit.DAYS);

    }

    public Double getExpertRateAverage(Integer expertId){
        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("expert not found"));

        return expert.getTotal_rating()/expert.getCount_rating();
    }



    public String subscriptionEarning(Double earningMonth,Integer views){
    //for every *views in this  month, give these expert *earningMonth credit.
    List<Post> recentContent = postRepository.getRecentMonthLearningPosts(LocalDateTime.now().minusMonths(1),views);
    Double credit=0.0; //for every x views
    Integer countPost=0;

    for(Post post : recentContent){

    countPost++;
    credit = Math.floor(post.getViews() * 1.0 / views)*earningMonth;
    post.getUsers().setBalance(post.getUsers().getBalance()+credit);
    postRepository.save(post);

    }
    return "total of "+countPost+" learning content has achieved +"+views+"Views. Credit sent to expert successfully";
    }

}



