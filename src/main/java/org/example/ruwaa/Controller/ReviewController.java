package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.DTOs.ReviewDTO;
import org.example.ruwaa.Service.PaymentService;
import org.example.ruwaa.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController
{
    private final ReviewService reviewService;


    @GetMapping("/finished")
    public ResponseEntity<?> getFinishedReviews(){
        return ResponseEntity.status(200).body(reviewService.getFinishedReviews());
    }

    @GetMapping("/unfinished")
    public ResponseEntity<?> getUnfinishedReviews(){
        return ResponseEntity.status(200).body(reviewService.getUnfinishedReviews());
    }
// *************************
//    @PutMapping("/request-review/{postId}/{expertId}")
//    public ResponseEntity<?> reviewMyWork (@PathVariable Integer postId, @PathVariable Integer expertId, Authentication auth) {
//
//        reviewService.requestReview(postId, expertId);
//        return ResponseEntity.status(200).body(new ApiResponse("Request send successfully"));
//    }
    @PutMapping("/rate/{reviewId}/{rate}")
    public ResponseEntity<?> rateReview(@PathVariable Integer reviewId,@PathVariable Integer rate) {
        reviewService.rateReview(reviewId, rate);
        return ResponseEntity.status(200).body("Review rated to "+rate+" successfully");
    }


    @PostMapping("/request/{expertId}/{workId}")
    public ResponseEntity<?> requestReview(@PathVariable Integer expertId,@PathVariable Integer workId) {
        reviewService.requestReview(expertId, workId);
        return ResponseEntity.status(200).body("Review request sent successfully");
    }

    @PutMapping("/submit/{reviewId}/{userId}")
    public ResponseEntity<?> submitReview(@PathVariable Integer reviewId,@PathVariable Integer userId,@RequestBody ReviewDTO reviewDTO) {
        reviewService.submitReview(reviewId, userId, reviewDTO);
        return ResponseEntity.status(200).body("Review submitted successfully");
    }


    @GetMapping("/template/{postId}")
    public ResponseEntity<?> makeReviewTemplate(@PathVariable Integer postId) {
        return ResponseEntity.status(200).body(new ApiResponse(reviewService.makeReviewTemplate(postId)));
    }


    @GetMapping("/assist/{postId}")
    public ResponseEntity<?> reviewAssistance(@PathVariable Integer postId) {
        return ResponseEntity.status(200).body(new ApiResponse(reviewService.reviewAssistance(postId)));
    }

}
