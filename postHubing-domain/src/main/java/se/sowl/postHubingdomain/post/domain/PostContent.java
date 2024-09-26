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
    @MapsId // 일대일관계에서 사용
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    @Lob //content와 같은 대용량 텍스트 데이터를 저장하기 위해 사용함 
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
