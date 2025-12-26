package org.example.ruwaa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Service.MediaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController
{
    private final MediaService mediaService;

    @PostMapping("/post")
    public void postMedia(@RequestBody @Valid Post post, @AuthenticationPrincipal Authentication auth){
        mediaService.addMedia(auth.getName(), post);

    }
}
