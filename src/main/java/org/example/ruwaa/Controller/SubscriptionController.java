package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController
{
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(Authentication auth){
        subscriptionService.subscribe(auth.getName());
        return ResponseEntity.status(200).body(new ApiResponse("thank you for subscribing"));
    }

    @GetMapping("/my-subscription")
    public ResponseEntity<?> getMySubscription(Authentication auth){
        return ResponseEntity.status(200).body(subscriptionService.getSubscription(auth.getName()));
    }
}
