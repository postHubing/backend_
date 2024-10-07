package se.sowl.postHubingapi.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.post.service.EditPostService;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@WebMvcTest(EditPostController.class)
class EditPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EditPostService editPostService;

    @MockBean
    private OAuthService oAuthService;

    private CustomOAuth2User customOAuth2User;

    private User testUser;

    private Post testPost;

    private EditPostRequest testRequest;

    private PostDetailResponse testResponse;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        testUser = UserFixture.createUser(1L, "테스트", "테스트유저", "test@example.com", "naver");
        testUser = userRepository.save(testUser);

        testPost = Post.builder()
                .title("테스트코드")
                .userId(testUser.getId())
                .build();
    }

    @Nested
    @DisplayName("게시물 수정 및 생성테스트 ")
    class EditPostTest{
        @Test
        @DisplayName("Post /api/posts/edit, 새 게시물")
        void createNewPost() throws Exception{
            //given
            testRequest = EditPostRequest.builder()
                    .title("새 제목")
                    .content("새 내용")
                    .build();

            testResponse = PostDetailResponse.builder()
                    .id(2L)
                    .title("새 게시물")
                    .content("새 내용")
                    .createAt(LocalDateTime.now())
                    .authorName(testUser.getName())
                    .build();

            //when
            when(editPostService.editPost(testUser.getId(), testRequest)).thenReturn(testResponse);

            //then
            mockMvc.perform(post("/api/posts/edit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testRequest))
                            .with(csrf())
                            .principal(testUser))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(testPost.getId()))
                    .andExpect(jsonPath("$.result.title").value("테스트 제목"))
                    .andExpect(jsonPath("$.result.content").value("테스트내용"))
                    .andExpect(jsonPath("$.result.authorName").value(testUser.getName()));

        }

        @Test
        @DisplayName("POST /api/posts/edit - 기존 게시물 수정")
        void updateExistingPost() throws Exception{
            //given
            testRequest = EditPostRequest.builder()
                    .postId(1L)
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .build();
            testResponse = PostDetailResponse.builder()
                    .id(1L)
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .createAt(LocalDateTime.now())
                    .authorName(testUser.getName())
                    .build();
            //when
            when(editPostService.editPost(testUser.getId(), testRequest)).thenReturn(testResponse);

            //then
            mockMvc.perform(post("/api/posts/edit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testRequest))
                            .with(csrf())
                            .principal(testUser))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.result.id").value(testPost.getId()))
                    .andExpect(jsonPath("$.result.title").value("수정된 제목"))
                    .andExpect(jsonPath("$.result.content").value("수정된 내용"))
                    .andExpect(jsonPath("$.result.authorName").value(testUser.getName()));
        }
    }

}
