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
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    @Lob
    private String content;

    @Builder
    public PostContent(Post post, String content) {
        this.post = post;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
