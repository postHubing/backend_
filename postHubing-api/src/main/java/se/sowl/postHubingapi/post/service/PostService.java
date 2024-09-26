package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.post.exception.UserException;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getPostList() { // 모드 게시물 조회
        return postRepository.findAll();
    }


    @Transactional
    public PostDetailResponse editPost(Long userId, EditPostRequest request){
        Post post;

        if(request.getPostId() == null){
            post = createNewPost(userId, request);
        } else{
            post = updateExistingPost(userId, request);
        }

        return createPostDetailResponse(userId, post);

    }

    private Post createNewPost(Long userId, EditPostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);
        Post newPost = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(user)
                .build();
        return postRepository.save(newPost);
    }


    private Post updateExistingPost(Long userId, EditPostRequest request) {
        Post existingPost = postRepository.findById(request.getPostId())
                .orElseThrow(PostException.PostNotFoundException::new);
        validatePostOwnerShip(existingPost, userId);
        existingPost.update(request.getTitle(), request.getContent());
        return existingPost;
    }


    private PostDetailResponse createPostDetailResponse(Long userId, Post post) {
        return PostDetailResponse.from(post);
    }


    private void validatePostOwnerShip(Post post, Long userId){
        if(!post.getAuthor().getId().equals(userId)){
            throw new PostException.PostNotAuthorizedException();
        }
    }



}

