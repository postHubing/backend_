package se.sowl.postHubingdomain.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import se.sowl.postHubingdomain.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter @Setter
@Table(name = "posts")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String thumbnailUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostContent postContent;

    @Builder
    public Post(Long id, String title, Long userId, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
    }


    public void update(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    public void setPostContent(PostContent postContent) {
        this.postContent = postContent;
        if (postContent != null && postContent.getPost() != this) {
            postContent.setPost(this);
        }
    }

}