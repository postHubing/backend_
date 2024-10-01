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
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private List<PostComment> testPostCommentList;

    @BeforeEach
    void setUp(){
        testUser = UserFixture.createUser(1L, "테스트", "테스트유저", "test@example.com", "naver");
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
        when(oAuthService.loadUser(any())).thenReturn(customOAuth2User);

        testPost = Post.builder()
                .title("테스트코드")
                .author(testUser)
                .build();
        testPostCommentList = new ArrayList<>();
        for (int i=1; i<=3; i++){
            testPostCommentList.add(
                    PostComment.builder()
                            .user(testUser)
                            .post(testPost)
                            .content("게시판 내용"+i)
                            .build()
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

}