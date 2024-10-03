package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostCommentResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.post.repository.PostCommentRepository;
import se.sowl.postHubingdomain.post.repository.PostRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostRepository postRepository;

    @Transactional
    public List<PostCommentResponse> getCommentsByPostId(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        List<PostComment> comments = postCommentRepository.findByPostId(post.getId());

        return comments.stream()
                .map(PostCommentResponse::from)
                .toList();
    }

}
