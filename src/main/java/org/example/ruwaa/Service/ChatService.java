package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Chat;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ChatRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService
{
    private final ChatRepository chatRepository;
    private final UsersRepository usersRepository;

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

    public void closeChat(Integer chat_id,String username){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Chat c =  chatRepository.findById(chat_id).orElseThrow(() -> new ApiException("chat not found"));
        c.setIsOpen(false);
    }
}
