package com.uvg.earth.way.controller;

import com.uvg.earth.way.model.Post;
import com.uvg.earth.way.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
                                           @RequestBody Post post,
                                           @RequestParam Long userId) {
        return ResponseEntity.ok(postService.updatePost(id, post, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> listPosts() {
        return ResponseEntity.ok(postService.listPosts());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeletePostById(@PathVariable Long id) {
        postService.adminDeletePostById(id);
        return ResponseEntity.noContent().build();
    }
}
