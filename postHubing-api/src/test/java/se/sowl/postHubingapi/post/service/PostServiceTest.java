package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.response.PostDetailResponse;
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

            Post post = Post.builder()
                    .title(i+"번째 게시판 제목입니다.")
                    .author(testUser)
                    .content(i + "번째 게시판 내용입니다.")
                    .build();

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
            List<Post> postLists = postService.getPostList();

            //then
            assertEquals(5, postLists.size(),"저장된 게시물은 5개이여야합니다.");
        }

    }

    @Nested
    @DisplayName("게시판 생성 및 수정")

    class EditPost{
        @Test
        @DisplayName("새 게시물 생성")
        void createNewPost(){
            //given
            EditPostRequest request = new EditPostRequest(null, "새 게시물", "새 내용");

            //when
            PostDetailResponse response = postService.editPost(testUser.getId(),request);

            //then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getContent(), response.getContent());
            assertEquals(testUser.getId(), response.getAuthorId());

            Post savedPost = postRepository.findById(response.getId()).orElse(null);
            assertNotNull(savedPost);
            assertEquals(request.getTitle(),response.getTitle());
            assertEquals(request.getContent(), response.getContent());
            assertEquals(testUser.getId(), response.getAuthorId());
        }
    }

    @Test
    @DisplayName("기존 게시물 수정")
    void updateExistingPost(){
        //given
        Post existingPost = testPosts.get(0);
        EditPostRequest request = new EditPostRequest(existingPost.getId(), "수정된 게시물", "수정된 내용");

        //when
        PostDetailResponse response = postService.editPost(testUser.getId(), request);

        //then
        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContent(), response.getContent());
        assertEquals(testUser.getId(), response.getAuthorId());

        Post updatedPost = postRepository.findById(existingPost.getId()).orElse(null);
        assertNotNull(updatedPost);
        assertEquals(request.getTitle(), updatedPost.getTitle());
        assertEquals(request.getContent(), updatedPost.getPostContent().getContent());
    }


}