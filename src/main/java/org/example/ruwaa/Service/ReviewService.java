package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.PostRepository;
import org.example.ruwaa.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private final ReviewRepository reviewRepository;
    private final PostRepository mediaRepository;
    private final ExpertRepository expertRepository;

    public List<Review> getAll(){
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()){
            throw new ApiException("no reviews found");
        }
        return reviews;
    }

    public void add(Integer expert_id,Integer media_id,Review review){
        Expert e = expertRepository.findExpertById(expert_id).orElseThrow(() -> new ApiException("expert not found"));

        Post m = mediaRepository.findMediaById(media_id).orElseThrow(() -> new ApiException("media not found"));

        review.setPost(m);
        review.setExpert(e);
        reviewRepository.save(review);
    }

    public void update(Integer id, Review review){
        Review r = reviewRepository.findReviewById(id).orElseThrow(() -> new ApiException("review not found"));

        r.setContent(review.getContent());
        reviewRepository.save(r);
    }

    public void delete(Integer id){
        Review r = reviewRepository.findReviewById(id).orElseThrow(() -> new ApiException("review not found"));

        reviewRepository.delete(r);
    }
}
