package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Service.ExpertService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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


    @GetMapping("/get-expert-by-category/{category}")
    public ResponseEntity<?> getExpertByCategory (Authentication auth, @PathVariable String category) {
        return ResponseEntity.status(200).body(expertService.getExpertByCategory(auth.getName(), category));
    }

    @PostMapping("/discount/{discountPercentage}/{date}")
    public ResponseEntity<?> applyDiscount(Authentication auth, @PathVariable Double discountPercentage, @PathVariable LocalDate date) {

        expertService.applyDiscount(auth.getName(), discountPercentage,date);
        return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully"));
    }


    @GetMapping("/get-high-rated-by-category/{category}")
    public ResponseEntity<?> getHighRatedExpertByCategory (Authentication auth, @PathVariable String category) {
        return ResponseEntity.status(200).body(expertService.getHighRatedExpertByCategory(auth.getName(), category));
    }


    @GetMapping("/calculate-average/{someExpert}")
    public ResponseEntity<?> getExpertRateAverage(@PathVariable Integer someExpert){
    return ResponseEntity.status(200).body(expertService.getExpertRateAverage(someExpert));
    }

    @PutMapping("/subscription-earning/{earningMonth}/{views}")
    public ResponseEntity<?> subscriptionEarning(@PathVariable Double earningMonth, @PathVariable Integer views){
    return ResponseEntity.status(200).body(new ApiResponse(expertService.subscriptionEarning(earningMonth,views)));
    }
}
