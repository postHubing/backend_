package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

}
