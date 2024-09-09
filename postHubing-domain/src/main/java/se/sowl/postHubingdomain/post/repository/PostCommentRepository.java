package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
