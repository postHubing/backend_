package se.sowl.postHubingapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import se.sowl.postHubingdomain.post.domain.PostComment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostCommentResponse {
    private Long id;
    private Long userId;
    private Long postId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public static PostCommentResponse from (PostComment postComment){
        return new PostCommentResponse(
                postComment.getId(),
                postComment.getUser().getId(),
                postComment.getPost().getId(),
                postComment.getUser().getName(),
                postComment.getContent(),
                postComment.getCreatedAt()
        );
    }
}
