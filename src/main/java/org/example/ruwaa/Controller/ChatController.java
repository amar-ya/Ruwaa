package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Model.Chat;
import org.example.ruwaa.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController
{
    private final ChatService chatService;


    @GetMapping("/get")
    public ResponseEntity<?> getAll(Authentication auth){
        System.out.println("authority:"+auth.getAuthorities()+"\nname:"+auth.getName()+"credentials:"+auth.getCredentials());
        return ResponseEntity.status(200).body(chatService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Chat chat){

        chatService.save(chat);
        return ResponseEntity.status(200).body(chat);
    }

}
