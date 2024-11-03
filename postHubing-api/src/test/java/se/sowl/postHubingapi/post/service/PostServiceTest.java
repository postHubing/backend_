package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.PostFixture;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    private List<Post> testPosts;


    @BeforeEach
    void setUp(){

        testUser = UserFixture.createUser(null, "테스트1", "테스트유저1", "test1@example.com", "naver");
        testUser = userRepository.save(testUser);

        testPosts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {

            Post post = PostFixture.createPost(null, "테스트 게시물" + i, "테스트 컨텐츠" + i, testUser.getId());
            testPosts.add(post);
        }
        postRepository.saveAll(testPosts);

    }

    @Nested
    @DisplayName("게시판 조회")

    class getPost{

        @Test
        @DisplayName("모든 게시판 조회")
        void getAllPost(){
            //given

            //when
            List<PostListResponse> postListResponses = postService.getPostList();

            //then
            assertEquals(5, postListResponses.size(),"저장된 게시물은 5개이여야합니다.");
        }

    }

    @Nested
    @DisplayName("게시물 상세 조회")
    class getPostDetail{
        @Test
        @DisplayName("게시물 상세 조회")
        void getTestPostDetail(){
            //given
            Post testPost = testPosts.get(0);

            //when
            PostDetailResponse response = postService.getPostDetail(testPost.getId());

            //then
            assertEquals(testPost.getTitle(), response.getTitle(), "게시물 제목이 일치해야합니다.");
            assertEquals(testPost.getPostContent().getContent(), response.getContent(), "게시물 내용이 일치해야합니다.");
            assertEquals(testUser.getNickname(), response.getAuthorName(), "게시물 작성자 닉네임이 일치해야합니다.");
        }

        @Test
        @DisplayName("존재하지 않는 게시물 조회 시 예외 발생")
        void getPostDetailNotFound() {
            // given
            Long nonExistentPostId = 999999L;

            // when & then
            assertThrows(PostException.PostNotFoundException.class,
                    () -> postService.getPostDetail(nonExistentPostId),
                    "존재하지 않는 게시물 조회 시 PostNotFoundException이 발생해야 합니다.");
        }
    }

}