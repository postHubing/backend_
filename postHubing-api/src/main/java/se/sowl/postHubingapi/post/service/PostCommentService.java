package se.sowl.postHubingapi.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

        if (!postRepository.existsById(postId)) {
            throw new EntityNotFoundException("게시물을 찾을 수 없습니다." + postId + "없는 아이디입니다.");
        }

        return postCommentRepository.findByPostId(postId);
    }

}
