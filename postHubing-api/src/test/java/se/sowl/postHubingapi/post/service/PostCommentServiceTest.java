package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.post.repository.PostCommentRepository;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostCommentServiceTest {

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private List<PostComment> testPostComments;

    private User testUser;

    private Post testPost;

    @BeforeEach
    void setUp(){
        testUser = UserFixture.createUser(null, "테스트1", "테스트유저1", "test1@example.com", "naver");
        testUser = userRepository.save(testUser);

        testPost = Post.builder()
                .title("testPost")
                .author(testUser)
                .build();
        testPost = postRepository.save(testPost);
    }

    @Nested
    @DisplayName("댓글 조회")

    class getPostComment{
        @Test
        @DisplayName("게시판에 댓글이 있는 경우")
        void testExistingComment(){
            //given
            testPostComments = new ArrayList<>();
            for(int i=1; i<=3; i++){
                PostComment postComment = PostComment.builder()
                        .post(testPost)
                        .user(testUser)
                        .content("테스트 댓글 내용"+i)
                        .build();
                testPostComments.add(postComment);
            }
            postCommentRepository.saveAll(testPostComments);
            //when
            List<PostComment> postCommentList = postCommentService.getCommentsByPostId(testPost.getId());

            //then
            assertEquals(3,postCommentList.size(),"게시판 내의 댓글은 3개이여야합니다.");
        }

        @Test
        @DisplayName("게시판에 댓글이 없는 경우")
        void testNullExistingComment(){
            //given

            //when
            List<PostComment> postCommentList = postCommentService.getCommentsByPostId(testPost.getId());
            //then
            assertEquals(0,postCommentList.size(),"게시판 내의 댓글이 없습니다.");
        }


    }
}