package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.PostComment;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);
}
