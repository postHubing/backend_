package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostContent;
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
            PostContent postContent = PostContent.builder()
                    .content(i+"번째 게시판 내용입니다.")
                    .build();

            Post post = Post.builder()
                    .title(i+"번째 게시판 제목입니다.")
                    .author(testUser)
                    .build();
            post.setPostContent(postContent);
            postContent.setPost(post);

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
            List<Post> postLists = postService.getAllPosts();

            //then
            assertEquals(5, postLists.size(),"저장된 게시물은 5개이여야합니다.");
        }


    }

}