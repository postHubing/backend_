package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostContent;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class EditPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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

        Post newPost = Post.builder()
                .title(request.getTitle())
                .userId(userId)
                .thumbnailUrl(request.getThumbnailUrl())
                .build();

        PostContent newContent = new PostContent(newPost, request.getContent());
        newPost.setPostContent(newContent);

        return postRepository.save(newPost);
    }


    private Post updateExistingPost(Long userId, EditPostRequest request) {
        Post existingPost = findPostById(request.getPostId());

        validatePostOwnership(existingPost, userId);
        existingPost.update(request.getTitle());
        existingPost.setThumbnailUrl(request.getThumbnailUrl());

        PostContent postContent = existingPost.getPostContent();
        if (postContent == null) {
            postContent = new PostContent(existingPost, request.getContent());
            existingPost.setPostContent(postContent);
        } else {
            postContent.update(request.getContent());
        }

        return postRepository.save(existingPost);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostException.PostNotFoundException::new);
    }


    private PostDetailResponse createPostDetailResponse(Long userId, Post post) {
        Long writerId = post.getUserId();
        String writerNickname = getWriterNickname(writerId);
        return PostDetailResponse.from(post, writerNickname);
    }

    private String getWriterNickname(Long writerId) {
        return userRepository.findById(writerId)
                .map(user -> user.getNickname() != null ? user.getNickname() : user.getName())
                .orElse("탈퇴한 사용자");
    }


    private void validatePostOwnership(Post post, Long userId) {
        if (!post.getUserId().equals(userId)) {
            throw new PostException.PostNotAuthorizedException();
        }
    }
}
