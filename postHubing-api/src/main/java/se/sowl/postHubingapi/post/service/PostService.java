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


    // 메서드 분리
    // upsertPost 메서드에 코드가 너무 길어
    /*if (request.getId() == null) {
        post = createNewPost(userId, request);
    } else {
        post = updateExistingPost(userId, request);*/
    // 위 코드 참고해서 메서드 분리 및 추가

    // upsertPost 메서드 이름 변경 -> editPost
    // editPost 메서드 파람 변경 -> userId, EditPostRequest (postId, title, content, author)
    // EditPostRequest 추가하기

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

        // Fixture는 테스트 코드에서 사용하는 것
        // PostDetailResponse 추가 생성해서 리턴
        // Response의 형태는 fixture와 유사
        return PostFixture.from(savedPost);

    }
}
