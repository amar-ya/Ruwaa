package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
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

    @PutMapping("/request-review/{postId}/{expertId}")
    public ResponseEntity<?> requestReview (@PathVariable Integer postId, @PathVariable Integer expertId, Authentication auth) {

        reviewService.requestReview(postId, expertId);
        return ResponseEntity.status(200).body(new ApiResponse("Request send successfully"));
    }


}
