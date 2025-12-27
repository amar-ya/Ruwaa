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

@Service
@RequiredArgsConstructor
public class ExpertService
{
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

    public void acceptReview (Integer reviewId, Review review) {
        Review review1 = reviewRepository.findReviewById(reviewId).orElseThrow(() -> new ApiException("review not found"));

        review1.setStatus("Accepted");
        review1.setContent(review.getContent());
        reviewRepository.save(review1);
    }


    public void rejectReview (Integer reviewId) {
        Review review = reviewRepository.findReviewById(reviewId).orElseThrow(() -> new ApiException("review not found"));

        review.setStatus("Rejected");
    }


    public void rejectAll (Integer expertId) {
        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("expert not found"));

        List<Review> reviews = reviewRepository.findAllByExpert(expert);

        for (Review review : reviews) {
            if (review.getStatus().equals("Pending")) {
                review.setStatus("Rejected");
            }
        }
        reviewRepository.saveAll(reviews);
    }
}
