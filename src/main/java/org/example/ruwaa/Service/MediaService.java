package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.MediaRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService
{
    private final MediaRepository mediaRepository;
    private final UsersRepository usersRepository;

    public List<Post> getAll(){
        List<Post> postList = mediaRepository.findAll();
        if (postList.isEmpty()){
            throw new ApiException("expert not found");
        }
        return postList;
    }

    public void addMedia(String username, Post post){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));

        post.setUsers(u);
        mediaRepository.save(post);
    }

    public void update(Integer id, Post post){
        Post m = mediaRepository.findMediaById(id).orElseThrow(() -> new ApiException("media not found"));

        m.setTitle(post.getTitle());
        m.setContent(post.getContent());
        mediaRepository.save(m);
    }

    public void deleteMedia(Integer id){
        Post m = mediaRepository.findMediaById(id).orElseThrow(() -> new ApiException("media not found"));

        mediaRepository.delete(m);
    }
}
