package org.example.ruwaa.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.Model.Message;
import org.example.ruwaa.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController
{
    private final MessageService messageService;

    @PostMapping("/send/{chat_id}")
    public ResponseEntity<?> sendMessage(@PathVariable Integer chat_id, @RequestBody Message message, Authentication auth){
        messageService.send(auth.getName(), chat_id,message);
        return ResponseEntity.status(200).body(new ApiResponse("message sent successfully"));
    }

    @GetMapping("/display-chat/{chat_id}")
    public ResponseEntity<?> displayChat(@PathVariable Integer chat_id, Authentication auth){
        return ResponseEntity.status(200).body(messageService.displayChat(chat_id));
    }
}
