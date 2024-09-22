package se.sowl.postHubingapi.post.fixture;

import lombok.Builder;
import lombok.Getter;
import se.sowl.postHubingdomain.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostFixture {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private String authorName;
    private Long authorId;

    public static PostFixture from(Post post){
        return builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getPostContent().getContent())
                .createAt(post.getCreatedAt())
                .authorName(post.getAuthor().getName())
                .authorId(post.getAuthor().getId())
                .build();
    }



}
