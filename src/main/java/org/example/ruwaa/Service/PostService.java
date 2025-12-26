package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.PostRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService
{
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    public List<Post> getAll(){
        List<Post> postList = postRepository.findAll();
        if (postList.isEmpty()){
            throw new ApiException("expert not found");
        }
        return postList;
    }

    public void addPost(String username, Post post){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));

        post.setUsers(u);
        postRepository.save(post);
    }

    public void updatePost(Integer id, Post post){
        Post m = postRepository.findMediaById(id).orElseThrow(() -> new ApiException("post not found"));

        m.setTitle(post.getTitle());
        m.setContent(post.getContent());
        postRepository.save(m);
    }

    public void deletePost(Integer id){
        Post m = postRepository.findMediaById(id).orElseThrow(() -> new ApiException("post not found"));

        postRepository.delete(m);
    }
}
