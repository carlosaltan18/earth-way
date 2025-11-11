package com.uvg.earth.way.service;

import com.uvg.earth.way.model.Post;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.PostRepository;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRespository;

    public PostService(PostRepository postRepository, UserRepository userRespository) {
        this.postRepository = postRepository;
        this.userRespository = userRespository;
    }

    public Post createPost(Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        post.setAuthor(user);
        post.setPostDate(LocalDate.now());

        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post postDetails, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id " + id));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para actualizar este post");
        }

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setImages(postDetails.getImages());
        return postRepository.save(post);
    }

    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id " + id));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para eliminar este post");
        }

        postRepository.delete(post);
    }

    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    public void adminDeletePostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id " + id));
        postRepository.delete(post);
    }
}
