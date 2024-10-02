package se.sowl.postHubingdomain.post.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Setter
@Table(name = "post_content")
public class PostContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public PostContent(Post post, String content) {
        this.setPost(post);
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
        if (post != null && post.getPostContent() != this) {
            post.setPostContent(this);
        }
    }

    public void update(String newContent) {
        this.content = newContent;
    }
}
