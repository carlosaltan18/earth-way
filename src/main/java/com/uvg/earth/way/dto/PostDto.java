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

        // Map author directly from the Post entity to avoid an extra DB lookup
        if (post.getAuthor() != null) {
            this.author = new UserPostDto(
                    post.getAuthor().getId(),
                    post.getAuthor().getName(),
                    post.getAuthor().getSurname(),
                    post.getAuthor().getEmail(),
                    post.getAuthor().getPhone()
            );
        }
    }
}
