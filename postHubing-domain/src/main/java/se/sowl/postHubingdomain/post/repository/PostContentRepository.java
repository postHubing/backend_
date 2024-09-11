package se.sowl.postHubingdomain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.post.domain.PostContent;

public interface PostContentRepository extends JpaRepository<PostContent,Long> {
}
