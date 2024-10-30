package se.sowl.postHubingapi.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingapi.response.PostCommentResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostCommentController.class)
class PostCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostCommentService postCommentService;

    @MockBean
    private OAuthService oAuthService;

    private CustomOAuth2User customOAuth2User;

    private User testUser;

    private Post testPost;

    private List<PostCommentResponse> testPostCommentList;

    @BeforeEach
    void setUp(){
        testUser = UserFixture.createUser(1L, "테스트", "테스트유저", "test@example.com", "naver");
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
        when(oAuthService.loadUser(any())).thenReturn(customOAuth2User);

        testPost = Post.builder()
                .title("테스트코드")
                .userId(testUser.getId())
                .build();
        testPostCommentList = new ArrayList<>();
        for (int i=1; i<=3; i++){
            testPostCommentList.add(
                    new PostCommentResponse(
                            (long) i,
                            testUser.getId(),
                            1L,
                            testUser.getName(),
                            "게시판 내용" + i,
                            LocalDateTime.now()
                    )
            );

        }

    }


    @Nested
    @DisplayName("게시물의 댓글 목록 조회 테스트")
    class GetPostCommentList{

        @Test
        @DisplayName("GET /api/postcomments/list, 댓글이 있는 경우")
        @WithMockUser
        void getPostCommentListTest() throws Exception{
            //given
            Long postId = 1L;

            //when
            when(postCommentService.getCommentsByPostId(postId)).thenReturn(testPostCommentList);

            //then
            mockMvc.perform(get("/api/postComments/list")
                            .param("postId",String.valueOf(postId))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result").isArray())
                    .andExpect(jsonPath("$.result.length()").value(3));
        }
        @Test
        @DisplayName("GET /api/postComments/list - 잘못된 postId")
        @WithMockUser
        void getPostCommentListWithInvalidPostIdTest() throws Exception {
            // Given
            Long invalidPostId = -1L;
            when(postCommentService.getCommentsByPostId(invalidPostId)).thenThrow(new IllegalArgumentException("Invalid postId"));

            // When & Then
            mockMvc.perform(get("/api/postComments/list")
                            .param("postId", String.valueOf(invalidPostId))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("Invalid postId"));
        }
    }


    @Nested
    @DisplayName("댓글 생성 테스트")
    class CreatePostComment{
        @Test
        @DisplayName("POST /api/postComments/create, 댓글 생성 성공")
        @WithMockUser
        void createPostCommentTest() throws Exception{
            //given
            Long postId = 1L;
            String content = "테스트 댓글 내용";
            //when
            when(postCommentService.createComment(postId, content, testUser.getId())).thenReturn(
                    new PostCommentResponse(
                            1L,
                            testUser.getId(),
                            postId,
                            testUser.getName(),
                            content,
                            LocalDateTime.now()
                    )
            );
            //then
            mockMvc.perform(post("/api/postComments/create")
                            .param("postId",String.valueOf(postId))
                            .param("userId",String.valueOf(testUser.getId()))
                            .content(content)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.userId").value(testUser.getId()))
                    .andExpect(jsonPath("$.result.postId").value(postId))
                    .andExpect(jsonPath("$.result.userName").value(testUser.getName()))
                    .andExpect(jsonPath("$.result.content").value(content));
        }

        @Test
        @DisplayName("POST /api/postComments/create - 잘못된 postId일 경우")
        @WithMockUser
        void createPostCommentWithInvalidPostIdTest() throws Exception {
            // Given
            Long invalidPostId = -1L;
            String content = "테스트 댓글 내용";
            when(postCommentService.createComment(invalidPostId, content, testUser.getId())).thenThrow(new IllegalArgumentException("잘못된 postId"));

            // When & Then
            mockMvc.perform(post("/api/postComments/create")
                            .param("postId", String.valueOf(invalidPostId))
                            .param("userId", String.valueOf(testUser.getId()))
                            .content(content)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("잘못된 postId"));
        }
        @Test
        @DisplayName("POST /api/postComments/create - 존재하지 않는 userId일 경우")
        @WithMockUser
        void createPostCommentWithInvaildUserIdTest() throws Exception{
            //given
            Long postId = 1L;
            String content = "테스트 댓글 내용";
            Long invalidUserId = -1L;

            //when
            when(postCommentService.createComment(postId, content, invalidUserId)).thenThrow(new IllegalArgumentException("잘못된 userId"));

            //then
            mockMvc.perform(post("/api/postComments/create")
                            .param("postId", String.valueOf(postId))
                            .param("userId", String.valueOf(invalidUserId))
                            .content(content)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("잘못된 userId"));
        }

        @Test
        @DisplayName("POST /api/postComments/create - 댓글 내용이 너무 짧은 경우")
        @WithMockUser
        void createPostCommentWithShortContentTest() throws Exception{
            //given
            Long postId = 1L;
            String content = "1";

            //when
            when(postCommentService.createComment(postId, content, testUser.getId())).thenThrow(new IllegalArgumentException("댓글 내용은 2자 이상이여야합니다."));

            //then
            mockMvc.perform(post("/api/postComments/create")
                            .param("postId", String.valueOf(postId))
                            .param("userId", String.valueOf(testUser.getId()))
                            .content(content)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("댓글 내용은 2자 이상이여야합니다."));
        }
    }



    @Nested
    @DisplayName("댓글 삭제 테스트")
    class DeletePostComment{
        @Test
        @DisplayName("POST /api/postComments/delete, 댓글 삭제 성공")
        @WithMockUser
        void deletePostCommentTest() throws Exception{
            //given
            Long postId = 1L;
            Long userId = testUser.getId();
            PostComment postComment = PostComment.builder()
                    .post(testPost)
                    .user(testUser)
                    .content("테스트 댓글 내용")
                    .build();
            //when
            when(postCommentService.deleteComment(postId, userId)).thenReturn(
                    new PostCommentResponse(
                            postComment.getId(),
                            testUser.getId(),
                            postId,
                            testUser.getName(),
                            postComment.getContent(),
                            postComment.getCreatedAt()
                    )
            );
            //then
            mockMvc.perform(post("/api/postComments/delete")
                            .param("postId", String.valueOf(postId))
                            .param("userId", String.valueOf(userId))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.userId").value(testUser.getId()))
                    .andExpect(jsonPath("$.result.postId").value(postId))
                    .andExpect(jsonPath("$.result.userName").value(testUser.getName()))
                    .andExpect(jsonPath("$.result.content").value(postComment.getContent()));
        }
        @Test
        @DisplayName("POST /api/postComments/delete - 잘못된 postId일 경우")
        @WithMockUser
        void deletePostCommentWithInvalidPostIdTest() throws Exception {
            // Given
            Long invalidPostId = -1L;
            Long userId = testUser.getId();

            // When & Then
            when(postCommentService.deleteComment(invalidPostId, userId)).thenThrow(new IllegalArgumentException("잘못된 postId"));

            mockMvc.perform(post("/api/postComments/delete")
                            .param("postId", String.valueOf(invalidPostId))
                            .param("userId", String.valueOf(userId))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("잘못된 postId"));
        }

        @Test
        @DisplayName("POST /api/postComments/delete - 존재하지 않는 userId일 경우")
        @WithMockUser
        void deletePostCommentWithInvalidUserIdTest() throws Exception {
            // Given
            Long postId = 1L;
            Long invalidUserId = -1L;

            // When & Then
            when(postCommentService.deleteComment(postId, invalidUserId)).thenThrow(new IllegalArgumentException("잘못된 userId"));

            mockMvc.perform(post("/api/postComments/delete")
                            .param("postId", String.valueOf(postId))
                            .param("userId", String.valueOf(invalidUserId))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("잘못된 userId"));
        }
    }

}