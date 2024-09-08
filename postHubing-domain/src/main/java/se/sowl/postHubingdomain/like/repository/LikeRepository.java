package se.sowl.postHubingdomain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.postHubingdomain.like.domain.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
