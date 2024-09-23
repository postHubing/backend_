package se.sowl.postHubingapi.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    // postComments로 변경
    // displayName 변경
    // 아래 적힌 부분은 nested내용이고
    // test 코드에서는 어떤 내용인지 알려줘야대
    // 게시물의 댓글 목록을 조회하는 테스트
    @DisplayName("GET /api/postcomments/list")
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
                // result가 배열로 리턴되는지 확인q
                .andExpect(jsonPath("$.result").isArray());

    }

    // 댓글이 없는 경우 테스트 추가

}