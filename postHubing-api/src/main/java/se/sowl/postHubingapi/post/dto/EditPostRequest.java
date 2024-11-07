package se.sowl.postHubingapi.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditPostRequest {

    private Long postId;
    private String title;
    private String content;
    private String thumbnailUrl;
}
