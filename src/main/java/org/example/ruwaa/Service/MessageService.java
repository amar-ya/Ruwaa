package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.DTOs.ChatBoxDTO;
import org.example.ruwaa.Model.Chat;
import org.example.ruwaa.Model.Message;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ChatRepository;
import org.example.ruwaa.Repository.MessageRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService
{
    private final MessageRepository messageRepository;
    private final UsersRepository usersRepository;
    private final ChatRepository chatRepository;

    public List<Message> getAll(){
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()){
            throw new ApiException(" no messages found");
        }
        return messages;
    }

    public void send(String username,Integer chat_id, Message m){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        Chat c = chatRepository.findChatById(chat_id).orElseThrow(() -> new ApiException("chat not found"));
        if (u != c.getReview().getPost().getUsers() && u != c.getReview().getExpert().getUsers()){
            throw new ApiException("you are not allowed to send this message");
        }
        if (!c.getIsOpen()){
            throw new ApiException("chat is closed");
        }

        m.setSent_at(LocalDateTime.now());
        m.setUsers(u);
        m.setChat(c);
        messageRepository.save(m);
    }

    public void update(Integer id, String text){
        Message m = messageRepository.findMessageById(id).orElseThrow(() -> new ApiException("message not found"));

        m.setText(text);
        messageRepository.save(m);
    }

    public void delete(Integer id){
        Message m = messageRepository.findMessageById(id).orElseThrow(() -> new ApiException("message not found"));

        messageRepository.delete(m);
    }

    public List<Message> displayChat(Integer chat_id){
       return messageRepository.findAllMessagesByChatId(chat_id).orElseThrow(() -> new ApiException("chat not found"));

    }
}
