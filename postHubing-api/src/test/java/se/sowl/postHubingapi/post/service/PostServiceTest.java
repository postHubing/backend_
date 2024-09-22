package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.fixture.PostFixture;
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

    private List<Post> posts;


    @BeforeEach
    void setUp(){

        // 테스트 유저 생성
        testUser = UserFixture.createUser(null, "테스트1", "테스트유저1", "test1@example.com", "naver");
        testUser = userRepository.save(testUser);

        posts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {

            Post post = Post.builder()
                    .title(i+"번째 게시판 제목입니다.")
                    .author(testUser)
                    .content(i + "번째 게시판 내용입니다.")
                    .build();

            posts.add(post);
        }
        postRepository.saveAll(posts);

    }

    @Nested  // 테스트 구조화
    @DisplayName("게시판 조회")

    class getPost{

        @Test
        @DisplayName("모든 게시판 조회")
        void getAllPost(){
            //given

            //when
            List<Post> postLists = postService.getPostList();

            //then
            assertEquals(5, postLists.size(),"저장된 게시물은 5개이여야합니다.");
        }

    }

    @Nested
    @DisplayName("게시판 생성 및 수정")

    class putPost{
        @Test
        @DisplayName("새 게시판 생성")
        void putPostSuccess(){

            //given
            String newTitle = "새로운 게시판 제목";
            String newContent = "새로운 게시판 내용";

            //when
            PostFixture newPost = postService.upsertPost(null, newTitle, newContent, testUser);

            //then
            assertNotNull(newPost);
            assertNotNull(newPost.getId());
            assertEquals(newTitle, newPost.getTitle());
            assertEquals(newContent, newPost.getContent());
            assertEquals(testUser.getId(), newPost.getAuthorId());
        }
        @Test
        @DisplayName("게시판 수정 성공")
        void updateExistingPost(){
            //given
            Post existingPost = posts.get(0);
            String updatedTitle = "수정된 제목";
            String updatedContent = "수정된 내용";

            //when
            PostFixture updatedPost = postService.upsertPost(existingPost.getId(), updatedTitle,updatedContent,testUser);

            //then
            assertNotNull(updatedPost);
            assertEquals(existingPost.getId(), updatedPost.getId());
            assertEquals(updatedTitle, updatedPost.getTitle());
            assertEquals(updatedContent, updatedPost.getContent());
            assertEquals(testUser.getId(), updatedPost.getAuthorId());

        }
    }

}