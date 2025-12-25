package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Service.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController
{
    private final ReviewService reviewService;
}
