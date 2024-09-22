package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.post.fixture.PostFixture;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostContent;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;

import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostList() { // 모드 게시물 조회
        return postRepository.findAll();
    }

    @Transactional
    public PostFixture upsertPost(Long postId, String title, String content, User author){
        Post post;
        if(postId != null){
            post = postRepository.findById(postId)
                    .orElseThrow(PostException.PostNotFoundException::new);
            post.setTitle(title);
            post.getPostContent().setContent(content);
        }
        else{
            post = Post.builder()
                    .title(title)
                    .author(author)
                    .content(content)
                    .build();
        }

        Post savedPost = postRepository.save(post);
        return PostFixture.from(savedPost);

    }
}
