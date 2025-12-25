package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Service.MediaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController
{
    private final MediaService mediaService;
}
