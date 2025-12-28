package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

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
        if (experts.isEmpty()) {
            throw new ApiException("No experts found");
        }

        return experts;
    }



    public void applyDiscount(Integer expertId, Double discountPercentage) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new ApiException("Expert not found"));

        Double originalPrice = expert.getConsult_price();

        double newPrice = originalPrice * (1 - discountPercentage / 100);
        expert.setConsult_price(newPrice);
        expertRepository.save(expert);

        scheduler.schedule(() -> {
            Expert e = expertRepository.findById(expertId).orElse(null);
            if (e != null) {
                e.setConsult_price(originalPrice);
                expertRepository.save(e);
            }
        }, 3, TimeUnit.DAYS);

    }

    public Double getExpertRateAverage(Integer expertId){
        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("expert not found"));

        return expert.getTotal_rating()/expert.getCount_rating();
    }

}



