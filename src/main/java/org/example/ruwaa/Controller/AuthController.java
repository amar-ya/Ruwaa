package org.example.ruwaa.Controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ruwaa.DTOs.AuthRequest;
import org.example.ruwaa.DTOs.RegisterCustomerRequest;
import org.example.ruwaa.DTOs.RegisterExpertRequest;
import org.example.ruwaa.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthRequest auth){
        return ResponseEntity.status(200).body(authService.login(auth));
    }

    @PostMapping("/signup/expert")
    public ResponseEntity<?> signupExpert (@RequestParam("cv") MultipartFile cv, @RequestBody @Valid RegisterExpertRequest auth){
        return ResponseEntity.status(200).body(authService.expertSignUp(cv, auth));
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> signupCustomer (@RequestBody @Valid RegisterCustomerRequest auth){
        return ResponseEntity.status(200).body(authService.registerCustomer(auth));
    }
}
