package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.CommentLike;

public interface PostCommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
