package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.dto.PostCommentRequest;
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
    public PostCommentResponse createComment(PostCommentRequest request) {
        if (request.getContent().length()<2){
            throw new PostException.CommentContentTooShortException();
        }

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostException.PostNotFoundException::new);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserException.UserNotFoundException::new);

        PostComment postComment = PostComment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        postCommentRepository.save(postComment);

        return PostCommentResponse.from(postComment);
    }

    @Transactional
    public PostCommentResponse deleteComment(PostCommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(PostException.PostNotFoundException::new);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserException.UserNotFoundException::new);

        PostComment postComment = postCommentRepository.findByPostAndUser(post, user)
                .orElseThrow(PostException.CommentNotFoundException::new);

        postCommentRepository.delete(postComment);

        return PostCommentResponse.from(postComment);
    }

}
