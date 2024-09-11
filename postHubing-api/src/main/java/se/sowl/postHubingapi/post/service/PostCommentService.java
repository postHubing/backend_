package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.post.repository.PostCommentRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    public List<PostComment> getCommentsByPostId(Long postId) {
        return postCommentRepository.findByPostId(postId);
    }

}
