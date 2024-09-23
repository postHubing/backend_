package se.sowl.postHubingapi.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.postHubingapi.post.exception.PostException;
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

    public List<PostComment> getCommentsByPostId(Long postId) {

        /*if (!postRepository.existsById(postId)) {
            throw new PostException.PostNotFoundException();
        }*/

        // exception 처리에서는 if문을 사용하지 않고, orElseThrow를 사용하는 것이 좋다.

        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        return postCommentRepository.findByPostId(post.getId());
    }

}
