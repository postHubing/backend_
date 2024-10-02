package se.sowl.postHubingapi.post.dto;

import se.sowl.postHubingdomain.post.domain.Post;

public class PostDTO {
    private Long id;
    private String title;
    private Long userId;
    private String content;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.userId = post.getUserId();
        this.content = post.getPostContent() != null ? post.getPostContent().getContent() : null;
    }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
}