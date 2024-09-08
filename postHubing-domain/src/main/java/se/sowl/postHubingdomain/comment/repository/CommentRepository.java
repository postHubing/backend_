package se.sowl.postHubingdomain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
