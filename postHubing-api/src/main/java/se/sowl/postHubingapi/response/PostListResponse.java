package se.sowl.postHubingapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se.sowl.postHubingdomain.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public static PostListResponse from(Post post){
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getThumbnailUrl(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUserId());
    }
}
