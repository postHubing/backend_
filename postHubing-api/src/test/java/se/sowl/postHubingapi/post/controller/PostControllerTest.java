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
import se.sowl.postHubingapi.post.service.PostService;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        PostListResponse post1 = new PostListResponse(1L, "testPost1", null, null, null, testUser.getId());
        PostListResponse post2 = new PostListResponse(2L, "testPost2", null, null, null ,testUser.getId());
        PostListResponse post3 = new PostListResponse(3L, "testPost3", null, null, null ,testUser.getId());


        testPostLists = Arrays.asList(post1, post2, post3);

    }

    @Test
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

    @Nested
    @DisplayName("게시물 상세 조회")
    class getTestPostDetail{
        @Test
        @DisplayName("GET /api/posts/detail - 게시물 상세 조회 성공")
        @WithMockUser
        void getTestPostDetailSuccess() throws Exception {
            // given
            Post testPost = Post.builder()
                    .id(1L)
                    .title("testPost1")
                    .userId(testUser.getId())
                    .build();

            PostDetailResponse response = PostDetailResponse.builder()
                    .id(testPost.getId())
                    .title("testPost1")
                    .content("testContent1")
                    .thumbnailUrl("testThumbnailUrl")
                    .createAt(LocalDateTime.now())
                    .authorName("테스트유저")
                    .build();
 
            // when
            when(postService.getPostDetail(testPost.getId())).thenReturn(response);

            // then
            mockMvc.perform(get("/api/posts/detail")
                            .param("postId", String.valueOf(testPost.getId()))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.title").value("testPost1"))
                    .andExpect(jsonPath("$.result.content").value("testContent1"))
                    .andExpect(jsonPath("$.result.thumbnailUrl").value("testThumbnailUrl"))
                    .andExpect(jsonPath("$.result.authorName").value("테스트유저"))
                    .andDo(print());
        }

        @Test
        @DisplayName("GET /api/posts/detail - 게시물 상세 조회 실패")
        @WithMockUser
        void getTestPostDetailFail() throws Exception {
            // given
            Post testPost = Post.builder()
                    .id(9999L)
                    .title("testPost1")
                    .userId(testUser.getId())
                    .build();

            PostDetailResponse response = PostDetailResponse.builder()
                    .id(testPost.getId())
                    .title("testPost1")
                    .content("testContent1")
                    .thumbnailUrl("testThumbnailUrl")
                    .createAt(LocalDateTime.now())
                    .authorName("테스트유저")
                    .build();
            // when
            when(postService.getPostDetail(testPost.getId())).thenThrow(new IllegalArgumentException("게시물을 찾을 수 없습니다."));

            // then
            mockMvc.perform(get("/api/posts/detail")
                            .param("postId", String.valueOf(testPost.getId()))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("게시물을 찾을 수 없습니다."))
                    .andDo(print());
        }

    }
    @Nested
    @DisplayName("게시물 검색")
    class searchPosts{
        @Test
        @DisplayName("GET /api/posts/search - 게시물 검색 성공")
        @WithMockUser
        void searchSuccessPosts() throws Exception{
            //given
            String keyword = "테스트";

            //when
            when(postService.searchPosts(keyword)).thenReturn(testPostLists);

            //then
            mockMvc.perform(get("/api/posts/search")
                            .param("keyword", keyword)
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

        @Test
        @DisplayName("GET /api/posts/search - 게시물 검색 실패")
        @WithMockUser
        void searchFailPosts() throws Exception{
            //given
            String keyword = "테스트";

            //when
            when(postService.searchPosts(keyword)).thenThrow(new IllegalArgumentException("검색 결과가 없습니다."));

            //then
            mockMvc.perform(get("/api/posts/search")
                            .param("keyword", keyword)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("FAIL"))
                    .andExpect(jsonPath("$.message").value("검색 결과가 없습니다."))
                    .andDo(print());
        }
    }
    @Nested
    @DisplayName("게시물 작성자 조회")
    class getPostsByUser{
        @Test
        @DisplayName("GET /api/posts/user - 본인 게시물 조회 성공")
        @WithMockUser
        void getPostsByUserSuccess() throws Exception{
            //given
            Long targetUserId = 1L;
            Long loggedInUserId = 1L;

            //when
            when(postService.getPostListByUserId(any())).thenReturn(testPostLists);

            //then
            mockMvc.perform(get("/api/posts/user")
                            .param("targetUserId", String.valueOf(targetUserId))
                            .param("loggedInUserId", String.valueOf(loggedInUserId))
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
        @Test
        @DisplayName("GET /api/posts/user - 타인 게시물 조회 성공")
        @WithMockUser
        void getPostsByUserFail() throws Exception{
            //given
            Long targetUserId = 2L;
            Long loggedInUserId = 1L;

            //when
            when(postService.getPostListByUserId(any())).thenReturn(testPostLists);

            //then
            mockMvc.perform(get("/api/posts/user")
                            .param("targetUserId", String.valueOf(targetUserId))
                            .param("loggedInUserId", String.valueOf(loggedInUserId))
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
}