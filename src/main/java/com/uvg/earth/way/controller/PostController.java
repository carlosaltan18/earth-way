package com.uvg.earth.way.controller;

import com.uvg.earth.way.model.Post;
import com.uvg.earth.way.service.PostService;
import com.uvg.earth.way.service.UserService;
import com.uvg.earth.way.dto.PostDto;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/post")

public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";


    @RolesAllowed({USER})
    @PostMapping("/post-post")
    public ResponseEntity<PostDto> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        PostDto postDto = new PostDto(createdPost, userService);
        return ResponseEntity.ok(postDto);
    }
    @RolesAllowed({USER})
    @PutMapping("/put-post/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
                                           @RequestBody Post post,
                                           @RequestParam Long userId) {
        return ResponseEntity.ok(postService.updatePost(id, post, userId));
    }
    @RolesAllowed({USER})
    @DeleteMapping("/delete-post/user/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-post")
    public ResponseEntity<List<Post>> listPosts() {
        return ResponseEntity.ok(postService.listPosts());
    }
    @RolesAllowed({ADMIN})
    @DeleteMapping("/delete-post/admin/{id}")
    public ResponseEntity<Void> adminDeletePostById(@PathVariable Long id) {
        postService.adminDeletePostById(id);
        return ResponseEntity.noContent().build();
    }
}
