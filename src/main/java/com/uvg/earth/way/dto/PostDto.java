package com.uvg.earth.way.dto;

import com.uvg.earth.way.model.Post;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.service.UserService;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private LocalDate postDate;
    private List<String> images;
    private UserPostDto author; // 🔄 Ahora usa el nuevo DTO

    public PostDto(Post post, UserService userService) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postDate = post.getPostDate();
        this.images = post.getImages();

        if (post.getAuthor() != null && post.getAuthor().getId() != null) {
            User userEntity = userService.findById(post.getAuthor().getId()).orElse(null);
            if (userEntity != null) {
                this.author = new UserPostDto(
                        userEntity.getId(),      
                        userEntity.getName(),
                        userEntity.getSurname(),
                        userEntity.getEmail(),
                        userEntity.getPhone()
                );
            }
        }
    }
}
