package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
