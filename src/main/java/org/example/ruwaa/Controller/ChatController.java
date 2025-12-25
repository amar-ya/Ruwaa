package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Model.Chat;
import org.example.ruwaa.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController
{
    private final ChatService chatService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body(chatService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Chat chat){
        chatService.save(chat);
        return ResponseEntity.status(200).body(chat);
    }

}
