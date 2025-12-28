package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Service.ExpertService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getExpertByCategory (@PathVariable String category) {
        return ResponseEntity.status(200).body(expertService.getExpertByCategory(category));
    }

    @PostMapping("/discount/{expertId}/{discountPercentage}/{date}")
    public ResponseEntity<?> applyDiscount(@PathVariable Integer expertId, @PathVariable Double discountPercentage, @PathVariable LocalDate date) {

        expertService.applyDiscount(expertId, discountPercentage,date);
        return ResponseEntity.status(200).body(new ApiResponse("Discount applied successfully"));
    }

    @GetMapping("/calculate-average/{someExpert}")
    public ResponseEntity<?> getExpertRateAverage(Integer someExpert){
    return ResponseEntity.status(200).body(expertService.getExpertRateAverage(someExpert));
    }

    @PutMapping("/subscription-earning/{earningMonth}/{views}")
    public ResponseEntity<?> subscriptionEarning(@PathVariable Double earningMonth, @PathVariable Integer views){
    return ResponseEntity.status(200).body(new ApiResponse(expertService.subscriptionEarning(earningMonth,views)));
    }
}
