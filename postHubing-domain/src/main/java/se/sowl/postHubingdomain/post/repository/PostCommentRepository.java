package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);

    Optional<PostComment> findByPostAndUser(Post post, User user);
}
