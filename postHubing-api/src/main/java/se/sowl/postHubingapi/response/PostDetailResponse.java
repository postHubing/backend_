package se.sowl.postHubingapi.response;

import lombok.Builder;
import lombok.Getter;
import se.sowl.postHubingdomain.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailResponse {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime createAt;
    private String authorName;

    public static PostDetailResponse from(Post post, String authorName) {
        return builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getPostContent().getContent())
                .thumbnailUrl(post.getThumbnailUrl())
                .createAt(post.getCreatedAt())
                .authorName(authorName)
                .build();
    }
}
