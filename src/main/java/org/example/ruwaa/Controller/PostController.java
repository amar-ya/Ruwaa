package org.example.ruwaa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiResponse;
import org.example.ruwaa.DTOs.LearningContentDTO;
import org.example.ruwaa.DTOs.WorkPostDTO;
import org.example.ruwaa.Model.Post;
import org.example.ruwaa.Service.PostService;
import org.example.ruwaa.Service.SendMailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController
{
    private final PostService postService;

//    @PostMapping("/create")
//    public ResponseEntity<?> postPost(@RequestBody @Valid Post post, Authentication auth){
//        System.out.println("\n\ncontroller:"+auth.getName()+"\n\n");
//       // mediaService.addPost(auth.getName(), post);
//        return ResponseEntity.status(200).body(new ApiResponse("Added"));
    //}
        @GetMapping("/get-all")
        public ResponseEntity<?> getAllPosts() {
          return   ResponseEntity.status(200).body(postService.getAll());
            }

    @GetMapping("/free-feed")
    public ResponseEntity<?> freeFeed() {
        return ResponseEntity.status(200).body(postService.freeFeed());
    }

    @GetMapping("/work-feed")
    public ResponseEntity<?> workFeed() {
        return ResponseEntity.status(200).body(postService.workFeed());
    }

    @GetMapping("/subscription-feed/{userId}")
    public ResponseEntity<?> subscriptionFeed(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(postService.subscribeFeed(userId));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(Authentication auth) {
        return ResponseEntity.status(200).body( postService.getMyPost(auth.getName()) );
    }

//=================
@GetMapping("/view/work/{userId}/{postId}")
public ResponseEntity<?> viewWorkPost(@PathVariable Integer userId, @PathVariable Integer postId) {
    return   ResponseEntity.status(200).body(postService.viewWorkPost(userId, postId));
}

    @GetMapping("/view/learning/{postId}")
    public ResponseEntity<?> viewLearningPost(Authentication auth, @PathVariable Integer postId) {
        return ResponseEntity.status(200).body(postService.viewLearningPost(auth.getName(), postId));
    }

    @PostMapping("/add/work")
    public ResponseEntity<?> addWorkPost(Authentication auth, @RequestBody WorkPostDTO dto) {
        System.out.println(dto.toString()+"\n"+auth);
        postService.addWorkPost(auth.getName(), dto);
       return   ResponseEntity.status(200).body(new ApiResponse("Work added :) ask for reviews!"));
    }

    @PostMapping("/add/learning")
    public ResponseEntity<?> addLearningContent(Authentication auth, @RequestBody LearningContentDTO dto) {
        postService.addLearningContent(auth.getName(), dto);
        return   ResponseEntity.status(200).body(new ApiResponse("Learning content added :)"));

    }


    @PutMapping("/update/work/{postId}")
    public ResponseEntity<?> updateWorkPost(@PathVariable Integer postId, @RequestBody WorkPostDTO dto) {
        postService.updateWorkPost(postId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Work updated!"));

    }

    @PutMapping("/update/learning/{postId}")
    public ResponseEntity<?> updateLearningContent(@PathVariable Integer postId, @RequestBody LearningContentDTO dto) {
        postService.updateLearningCont(postId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Learning content updated!"));

    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(200).body(new ApiResponse("post removed"));

    }


    @PutMapping("/public/{postId}")
    public ResponseEntity<?> makePublic(@PathVariable Integer postId) {
        postService.changeVisibilityToPublic(postId);
        return ResponseEntity.status(200).body(new ApiResponse("Work Visibility switched"));
    }

    @PutMapping("/private/{postId}")
    public ResponseEntity<?> makePrivate(@PathVariable Integer postId) {
        postService.changeVisibilityToPrivate(postId);
        return ResponseEntity.status(200).body(new ApiResponse("Work Visibility switched"));

    }


    @GetMapping("/review/{postId}")
    public ResponseEntity<?> reviewMyWork(@PathVariable Integer postId) {
        return ResponseEntity.status(200).body(new ApiResponse(postService.reviewMyWork(postId)));
    }



}
