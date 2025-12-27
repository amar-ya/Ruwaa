package org.example.ruwaa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Service.PostService;
import org.example.ruwaa.Service.SendMailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController
{
    private final PostService mediaService;

    @PostMapping("/create")
    public ResponseEntity<?> postPost(@RequestBody @Valid Post post, Authentication auth){
        System.out.println("\n\ncontroller:"+auth.getName()+"\n\n");
        mediaService.addPost(auth.getName(), post);
        return ResponseEntity.status(200).body(new ApiResponse("Added"));
    }
}
