package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {
    private final ReviewRepository reviewRepository;
    private final OpenAiService openAiService;


    public Map<String, Object> analyzeCustomerSkills(Integer customerId) {

        List<Review> all = reviewRepository.findAll();
        List<Review> reviews = new ArrayList<>();

        for (Review review : all) {
            if (review.getPost().getUsers().getId().equals(customerId)){
                reviews.add(review);
            }
        }

        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this customer");
        }

        String combinedReviews = reviews.stream()
                .map(Review::getContent)
                .collect(Collectors.joining("\n"));


        String prompt = """
                Analyze the following customer reviews and extract:
                - Skills
                - Strengths
                - Weaknesses
                - Overall rating from 1 to 5

                Reviews:
                %s
                """.formatted(combinedReviews);

        return openAiService.analyzeText(prompt);
    }
}
