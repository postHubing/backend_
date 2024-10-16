package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.post.exception.UserException;
import se.sowl.postHubingapi.response.PostCommentResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.post.repository.PostCommentRepository;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<PostCommentResponse> getCommentsByPostId(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        List<PostComment> comments = postCommentRepository.findByPostId(post.getId());

        return comments.stream()
                .map(PostCommentResponse::from)
                .toList();
    }

    @Transactional
    public PostCommentResponse createComment(Long postId, String content, Long userId) {
        if (content.length()<2){
            throw new PostException.CommentContentTooShortException();
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);

        PostComment postComment = PostComment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        postCommentRepository.save(postComment);

        return PostCommentResponse.from(postComment);


    }

}
