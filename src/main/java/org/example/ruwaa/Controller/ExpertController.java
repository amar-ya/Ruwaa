package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Service.ExpertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expert")
@RequiredArgsConstructor
public class ExpertController
{
    private final ExpertService expertService;

    @GetMapping("/most-active/category/{category}")
    public ResponseEntity<?> findMostActiveExpertByCategory(@PathVariable String category)
    {
        return ResponseEntity.status(200).body(expertService.findMostActiveExpertByCategory(category));
    }

    @PutMapping("/accept-review/{reviewId}")
    public ResponseEntity<?> acceptReview (@PathVariable Integer reviewId, @RequestBody Review review) {
        expertService.acceptReview(reviewId, review);
        return ResponseEntity.status(200).body(new ApiResponse("review send successfully"));
    }


    @PostMapping("/reject-review/{reviewId}")
    public ResponseEntity<?> rejectReview (@PathVariable Integer reviewId) {
        expertService.rejectReview(reviewId);
        return ResponseEntity.status(200).body(new ApiResponse("review rejected successfully"));
    }

    @PostMapping("/reject-all-reviews/{expertId}")
    public ResponseEntity<?> rejectAllReviews (@PathVariable Integer expertId) {
        expertService.rejectAll(expertId);
        return ResponseEntity.status(200).body(new ApiResponse("All reviews rejected successfully"));
    }
}
