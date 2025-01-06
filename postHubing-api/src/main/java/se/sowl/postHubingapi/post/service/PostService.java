package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<PostListResponse> getPostList() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostListResponse::from)
                .toList();
    }


    @Transactional
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        return createPostDetailResponse(post);
    }

    private PostDetailResponse createPostDetailResponse(Post post) {
        Long writerId = post.getUserId();
        String writerNickname = getWriterNickname(writerId);
        return PostDetailResponse.from(post, writerNickname);
    }

    private String getWriterNickname(Long writerId) {
        return userRepository.findById(writerId)
                .map(user -> user.getNickname() != null ? user.getNickname() : user.getName())
                .orElse("탈퇴한 사용자");
    }

    @Transactional
    public List<PostListResponse> searchPosts(String keyword) {
        List<Post> posts = postRepository.findAll();

        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getTitle().contains(keyword))
                .toList();

        return filteredPosts.stream()
                .map(PostListResponse::from)
                .toList();
    }

    @Transactional
    public List<PostListResponse> getPostListByUserId(Long targetUserId){
        List<Post> posts = postRepository.findByUserId(targetUserId);

        if (posts.isEmpty()) {
            throw new PostException.PostNotFoundException();
        }
        return posts.stream()
                .map(PostListResponse::from)
                .toList();
    }


    @Transactional
    public PostDetailResponse deletePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);

        postRepository.delete(post);
        return createPostDetailResponse(post);
    }
}

