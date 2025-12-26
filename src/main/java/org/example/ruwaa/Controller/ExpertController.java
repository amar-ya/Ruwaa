package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Service.ExpertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
