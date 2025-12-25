package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Service.ExpertService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expert")
@RequiredArgsConstructor
public class ExpertController
{
    private final ExpertService expertService;
}
