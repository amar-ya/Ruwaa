package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Chat;
import org.example.ruwaa.Repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService
{
    private final ChatRepository chatRepository;

    public List<Chat> getAll(){
        List<Chat> chats = chatRepository.findAll();
        if (chats.isEmpty()){
            throw new ApiException("there are no chats");
        }
        return chats;
    }

    public void save(Chat chat){
        chatRepository.save(chat);
    }
}
