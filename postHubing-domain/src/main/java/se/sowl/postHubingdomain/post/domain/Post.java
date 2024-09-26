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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private PostContent postContent;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> postComments = new ArrayList<>();

    @Builder
    public Post(Long id, String title, User author, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.postContent = new PostContent(this, content);
    }

//    public void update_(String title, String content) {
//        this.title = title;
//        this.postContent = new PostContent(this, content);
//        this.updatedAt = LocalDateTime.now();
//    }

    public void update(String title, String content) {
        this.title = title;
        if (this.postContent == null) {
            this.postContent = new PostContent(this, content);
        } else {
            this.postContent.update(content);
        }
        this.updatedAt = LocalDateTime.now();
    }

}