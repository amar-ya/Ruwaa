package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.DTOs.LearningContentDTO;
import org.example.ruwaa.DTOs.WorkPostDTO;
import org.example.ruwaa.Model.Categories;
import org.example.ruwaa.Model.Customer;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.CategoriesRepository;
import org.example.ruwaa.Repository.CustomerRepository;
import org.example.ruwaa.Repository.PostRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService
{
    private final CategoriesRepository categoriesRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;
    private final AiService aiService;


    public List<Post> getAll(){
        List<Post> postList = postRepository.findAll();
        if (postList.isEmpty()){
            throw new ApiException("expert not found");
        }
        return postList;
    }



    public void addWorkPost(String username, WorkPostDTO dto){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        //is only customers or both?      if(!u.getRole().equals("CUSTOMER")) throw new ApiException("only customers can add this content type");
        Categories category = categoriesRepository.findCategoryByName(dto.getCategory()).orElseThrow(() -> new ApiException("invalid category"));

        String type = (dto.getIsPublic()) ? "public_work":"private_work";
        HashSet<Users> permitPrivateWorkVisiablity;
        if(dto.getIsPublic()) permitPrivateWorkVisiablity=null;
        else {
            permitPrivateWorkVisiablity= new HashSet<Users>();
            permitPrivateWorkVisiablity.add(u); //adding the current user so they can see their own work :)
        }

        Post post = new Post(null,dto.getContent(),0,type,permitPrivateWorkVisiablity,u,null,dto.getAttachments(),category);
        postRepository.save(post);
    }


    public void addLearningContent(String username, LearningContentDTO dto){
        Users u = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        if(!u.getRole().equals("EXPERT")) throw new ApiException("only expert can add this content type");

        Categories category = categoriesRepository.findCategoryByName(dto.getCategory()).orElseThrow(() -> new ApiException("invalid category"));
        String type = (dto.getIsFree()) ? "free_content":"subscription_content";

        Post post = new Post(null,dto.getContent(),0,type,null,u,null,dto.getAttachments(),category);
        postRepository.save(post);
    }

    public void updateWorkPost(Integer id, WorkPostDTO dto){
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));

        post.setContent(dto.getContent());
        post.setAttachments(dto.getAttachments());

        postRepository.save(post);
    }
    public void updateLearningCont(Integer id, LearningContentDTO dto){
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));

        post.setContent(dto.getContent());
        post.setAttachments(dto.getAttachments());

        postRepository.save(post);
    }

    public void deletePost(Integer id){
        Post m = postRepository.findPostById(id).orElseThrow(() -> new ApiException("post not found"));

        postRepository.delete(m);
    }


    public Post viewWorkPost(Integer userId ,Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        Users user = usersRepository.findUserById(userId).orElseThrow(()-> new ApiException("user not found"));

    if(p.getType().equals("private_work"))
     if(!p.getPermitWorkVisiablity().contains(user)) throw new ApiException("This is private work");

    p.setViews(p.getViews()+1);
    postRepository.save(p);
    return p;
    }

    public Post viewLearningPost(String username ,Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        Users user = usersRepository.findUserByUsername(username).orElseThrow(()-> new ApiException("user not found"));

        if(p.getType().equals("subscription_content")) {

            if (user.getRole().equals("CUSTOMER")) {
                Customer customer = user.getCustomer();
                if (customer.getSubscription() == null || customer.getSubscription().getEnd_date().isBefore(LocalDateTime.now()))
                    throw new ApiException("this content needs subscription");

            }
            else if(!p.getUsers().equals(user)&&user.getRole().equals("EXPERT")) throw new ApiException("this content for customer");

        } //sub

        p.setViews(p.getViews()+1);
        postRepository.save(p);
        return p;
    }




    public List<Post> freeFeed(){
        return postRepository.findPostByType("free_content");
    }


    public List<Post> subscribeFeed(Integer userId){
        Users users = usersRepository.findUserById(userId).orElseThrow(()-> new ApiException("user not found"));
        if(users.getRole().equals("EXPERT")) throw new ApiException("subscription content feed allowed for customers only");
        if(!users.getRole().equals("ADMIN")){
            Customer customer = customerRepository.findCustomerById(userId).orElseThrow();
            if (customer.getSubscription() == null || customer.getSubscription().getEnd_date().isBefore(LocalDateTime.now()))
                throw new ApiException("this feed needs subscription");
        }
        return postRepository.findPostByType("subscription_content");
    }


    public List<Post> workFeed(){
        return postRepository.findPostByType("public_work");
    }





    public List<Post> getMyPost(String username){
        Users user = usersRepository.findUserByUsername(username).orElseThrow(() -> new ApiException("Account not found"));
        return postRepository.findPostByUsers_id(user.getId());
    }


    public String reviewMyWork(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");

        //AI

        return  aiService.askAI("Hello");
    }

    public void changeVisibilityToPublic(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if(p.getType().equals("public_work")) throw new ApiException("this work is already Public");

        p.setType("public_work");
        postRepository.save(p);

    }


    public void changeVisibilityToPrivate(Integer postId){
        Post p = postRepository.findPostById(postId).orElseThrow(() -> new ApiException("post not found"));
        if(!p.getType().equals("public_work")&&!p.getType().equals("private_work")) throw new ApiException("this is not work post");
        if(p.getType().equals("private_work")) throw new ApiException("this work is already Private");

        p.setType("private_work");
        postRepository.save(p);

    }



}
