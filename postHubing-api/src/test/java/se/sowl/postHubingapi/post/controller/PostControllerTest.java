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
import se.sowl.postHubingapi.fixture.PostFixture;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.post.dto.PostDTO;
import se.sowl.postHubingapi.post.service.PostService;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private OAuthService oAuthService;

    private User testUser;

    private List<PostListResponse> testPostLists;

    private CustomOAuth2User customOAuth2User;

    @BeforeEach
    void setUp(){
        testUser = UserFixture.createUser(1L, "테스트", "테스트유저", "test@example.com", "naver");
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
        when(oAuthService.loadUser(any())).thenReturn(customOAuth2User);

        PostListResponse post1 = new PostListResponse(1L, "testPost1", null, null, testUser.getId());
        PostListResponse post2 = new PostListResponse(2L, "testPost2", null, null, testUser.getId());
        PostListResponse post3 = new PostListResponse(3L, "testPost3", null, null, testUser.getId());


        testPostLists = Arrays.asList(post1, post2, post3);

    }

    @Test
    // 한글로 테스트 케이스 작성
    // 게시물 리스트 조회
    @DisplayName("GET /api/posts/list")
    @WithMockUser
    void getPostListTest() throws Exception{

        //given

        //when
        when(postService.getPostList()).thenReturn(testPostLists);

        //then
        mockMvc.perform(get("/api/posts/list")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(3))
                .andExpect(jsonPath("$.result[0].title").value("testPost1"))
                .andExpect(jsonPath("$.result[1].title").value("testPost2"))
                .andExpect(jsonPath("$.result[2].title").value("testPost3"));
    }
}