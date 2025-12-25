package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController
{
    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(customerService.findAll());
    }


}
