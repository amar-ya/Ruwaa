package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.PostRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Post m = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));

        m.setTitle(post.getTitle());
        m.setContent(post.getContent());
        postRepository.save(m);
    }

    public void deletePost(Integer id){
        Post m = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));

        postRepository.delete(m);
    }

    //1) 24
    public Post viewPost(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));

    p.setViews(p.getViews()+1);
    postRepository.save(p);
    return p;
    }
    //1)  1
    public List<Post> getMyPost(Integer userId){
        Users user = usersRepository.findUserById(userId).orElseThrow(() -> new ApiException("Account not found"));
        return postRepository.findPostByUsers_id(userId);
    }


    public String reviewMyWork(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");

        //AI
        return "";
    }
    //1)  6
    public void changeVisibilityToPublic(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if(p.getType().equals("public_work")) throw new ApiException("this work is already Public");

        p.setType("public_work");
        postRepository.save(p);

    }
}
