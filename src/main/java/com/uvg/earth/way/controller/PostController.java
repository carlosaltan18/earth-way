package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.PostDto;
import com.uvg.earth.way.model.Post;
import com.uvg.earth.way.service.PostService;
import com.uvg.earth.way.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";
    private final static String MESSAGE = "message";
    private final static String ERROR = "error";

    @RolesAllowed({USER})
    @PostMapping("/post-post")
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Post post) {
        Map<String, Object> response = new HashMap<>();
        try {
            Post createdPost = postService.createPost(post);
            PostDto postDto = new PostDto(createdPost, userService);
            response.put("payload", postDto);
            response.put(MESSAGE, "Post created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "Error creating post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({USER})
    @PutMapping("/put-post/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id,
                                                          @RequestBody Post post,
                                                          @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Post updatedPost = postService.updatePost(id, post, userId);
            PostDto postDto = new PostDto(updatedPost, userService);
            response.put("payload", postDto);
            response.put(MESSAGE, "Post updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "Error updating post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({USER})
    @DeleteMapping("/delete-post/user/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id,
                                                          @RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            postService.deletePost(id, userId);
            response.put(MESSAGE, "Post deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "Error deleting post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/listPost")
    public ResponseEntity<Map<String, Object>> listPosts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Post> posts = postService.listPosts();
            List<PostDto> postDtos = posts.stream()
                    .map(post -> new PostDto(post, userService))
                    .collect(Collectors.toList());

            response.put("payload", postDtos);
            response.put(MESSAGE, "Posts retrieved successfully");
            response.put("total", postDtos.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "Error retrieving posts: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN})
    @DeleteMapping("/delete-post/admin/{id}")
    public ResponseEntity<Map<String, Object>> adminDeletePostById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            postService.adminDeletePostById(id);
            response.put(MESSAGE, "Post deleted by admin successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "Error deleting post by admin: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
