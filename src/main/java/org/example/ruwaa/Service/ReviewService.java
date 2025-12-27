package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.*;
import org.example.ruwaa.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService
{
    private final ReviewRepository reviewRepository;
    private final PostRepository mediaRepository;
    private final ExpertRepository expertRepository;
    private final PostRepository postRepository;
    private final PaymentService paymentService;
    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;

    public List<Review> getAll(){
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()){
            throw new ApiException("no reviews found");
        }
        return reviews;
    }

    public void add(Integer expert_id,Integer media_id,Review review){
        Expert e = expertRepository.findExpertById(expert_id).orElseThrow(() -> new ApiException("expert not found"));

        Post m = mediaRepository.findPostById(media_id).orElseThrow(() -> new ApiException("media not found"));

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

    public void requestReview(Integer postId, Integer expertId) {

        Post post = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));

        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("expert not found"));


        Review review = new Review();
        review.setStatus("Pending");
        review.setExpert(expert);
        review.setPost(post);
        reviewRepository.save(review);
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


    public List<Review> getReviewsRequest (Integer expertId) {
        Expert expert = expertRepository.findExpertById(expertId).orElseThrow(() -> new ApiException("Expert not found"));

        List<Review> reviews = reviewRepository.findAllByExpert(expert);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return reviews;
    }


    public List<Review> getSendRequests (Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> new ApiException("Customer not found"));

        List<Review> customerRequests = new ArrayList<>();
        List<Review> all = reviewRepository.findAll();

        for (Review review : all) {
            if (review.getPost().getUsers().getId().equals(customerId)){
                customerRequests.add(review);
            }
        }
        return customerRequests;
    }

    public List<Review> getCompletedReviewsByPost(Integer postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("Post not found"));

        List<Review> reviews = reviewRepository.findAllByPost(post);
        List<Review> complete = new ArrayList<>();

        for (Review review : reviews) {
            if (review.getStatus().equals("Completed")) {
                complete.add(review);
            }
        }
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return complete;
    }
}
