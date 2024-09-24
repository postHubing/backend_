package se.sowl.postHubingapi.post.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPostRequest {

    private Long postId;
    private String title;
    private String content;
}
