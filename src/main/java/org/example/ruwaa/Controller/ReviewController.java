package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.DTOs.ReviewDTO;
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
    public ResponseEntity<?> getFinishedReviews(Authentication auth){
        return ResponseEntity.status(200).body(reviewService.getFinishedReviews(auth.getName()));
    }

    @GetMapping("/unfinished")
    public ResponseEntity<?> getUnfinishedReviews(Authentication auth){
        return ResponseEntity.status(200).body(reviewService.getUnfinishedReviews(auth.getName()));
    }


    @PutMapping("/rate/{reviewId}/{rate}")
    public ResponseEntity<?> rateReview(@PathVariable Integer reviewId,@PathVariable Integer rate) {
        reviewService.rateReview(reviewId, rate);
        return ResponseEntity.status(200).body("Review rated to "+rate+" successfully");
    }


    @PostMapping("/request/{expertId}/{workId}")
    public ResponseEntity<?> requestReview(Authentication auth, @PathVariable Integer expertId,@PathVariable Integer workId) {
        reviewService.requestReview(auth.getName(), expertId, workId);
        return ResponseEntity.status(200).body("Review request sent successfully");
    }

    @PutMapping("/submit/{reviewId}")
    public ResponseEntity<?> submitReview(@PathVariable Integer reviewId,Authentication auth,@RequestBody ReviewDTO reviewDTO) {
        reviewService.submitReview(reviewId, auth.getName(), reviewDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review submitted successfully"));
    }


    @GetMapping("/template/{postId}")
    public ResponseEntity<?> makeReviewTemplate(@PathVariable Integer postId) {
        return ResponseEntity.status(200).body(new ApiResponse(reviewService.makeReviewTemplate(postId)));
    }


    @PutMapping("/accept-review/{reviewId}")
    public ResponseEntity<?> acceptReview (Authentication auth, @PathVariable Integer reviewId) {
        reviewService.acceptReview(auth.getName(), reviewId);
        return ResponseEntity.status(200).body(new ApiResponse("review accepted successfully"));
    }


    @PostMapping("/reject-review/{review_id}")
    public ResponseEntity<?> rejectReview (Authentication auth, @PathVariable Integer review_id, @RequestBody String reason) {
        reviewService.rejectReview(auth.getName(), review_id,reason);
        return ResponseEntity.status(200).body(new ApiResponse("review rejected successfully"));
    }

    @PostMapping("/reject-all-reviews")
    public ResponseEntity<?> rejectAllReviews (Authentication auth) {
        reviewService.rejectAll(auth.getName());
        return ResponseEntity.status(200).body(new ApiResponse("All reviews rejected successfully"));
    }


    @GetMapping("/get-pending-review")
    public ResponseEntity<?> getPendingReviews (Authentication auth) {
        return ResponseEntity.status(200).body(reviewService.getPendingReviews(auth.getName()));
    }


    @GetMapping("/get-reviews-requests")
    public ResponseEntity<?> getReviewsRequest (Authentication auth) {
        return ResponseEntity.status(200).body(reviewService.getReviewsRequest(auth.getName()));
    }


    @GetMapping("/get-send-requests")
    public ResponseEntity<?> getSendRequests (Authentication auth) {
        return ResponseEntity.status(200).body(reviewService.getSentRequests(auth.getName()));
    }

    @GetMapping("/get-completed-reviews/{postId}")
    public ResponseEntity<?> getCompletedReviewsByPost (@PathVariable Integer postId, Authentication auth) {
        return ResponseEntity.status(200).body(reviewService.getCompletedReviewsByPost(postId, auth.getName()));
    }
    @GetMapping("/assist/{postId}")
    public ResponseEntity<?> reviewAssistance(@PathVariable Integer postId) {
        return ResponseEntity.status(200).body(new ApiResponse(reviewService.reviewAssistance(postId)));
    }

}
