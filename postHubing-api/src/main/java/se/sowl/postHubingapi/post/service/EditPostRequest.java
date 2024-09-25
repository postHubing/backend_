package se.sowl.postHubingapi.post.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditPostRequest {

    private Long postId;
    private String title;
    private String content;
}
