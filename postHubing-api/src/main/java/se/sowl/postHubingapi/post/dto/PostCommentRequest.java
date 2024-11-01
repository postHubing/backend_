package se.sowl.postHubingapi.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentRequest {

    Long userId;

    Long postId;

    String content;
}
