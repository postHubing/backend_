package se.sowl.postHubingapi.post.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.PostFixture;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostCommentResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.post.domain.PostContent;
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

        testPost = postRepository.save(PostFixture.createPost(null, "테스트 게시물", "테스트 컨텐츠", testUser.getId()));
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
            List<PostCommentResponse> postCommentList = postCommentService.getCommentsByPostId(testPost.getId());

            //then
            assertEquals(3,postCommentList.size(),"게시판 내의 댓글은 3개이여야합니다.");
        }

        @Test
        @DisplayName("게시판에 댓글이 없는 경우")
        void testNullExistingComment(){
            //given

            //when
            List<PostCommentResponse> postCommentList = postCommentService.getCommentsByPostId(testPost.getId());
            //then
            assertEquals(0,postCommentList.size(),"게시판 내의 댓글이 없습니다.");
        }

        @Test
        @DisplayName("게시판 아이디가 틀려 게시판을 찾을 수 없는 경우")
        void testNotExistingByPostId(){
            //given
            Long notExistingPostId = 9999L;

            //when & then
            assertThrows(PostException.PostNotFoundException.class,() -> postCommentService.getCommentsByPostId(notExistingPostId),
                    "존재하지 않는 ID로 조회시 PostNotFoundException가 발생해야 합니다.");

        }

    }
}