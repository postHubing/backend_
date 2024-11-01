package se.sowl.postHubingapi.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private UserRepository userRepository;

    private CustomOAuth2User customOAuth2User;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = UserFixture.createUser(1L, "테스트1", "테스트유저1", "test1@example.com", "naver");
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
    }

    @Test
    @DisplayName("Post /api/posts/edit, 새 게시물")
    void createNewPost() throws Exception {
        // given
        EditPostRequest request = EditPostRequest.builder()
                .title("새 게시물")
                .content("새 게시물 내용")
                .build();

        PostDetailResponse expectedResponse = PostDetailResponse.builder()
                .id(1L)
                .title("새 게시물")
                .content("새 게시물 내용")
                .createAt(LocalDateTime.now())
                .authorName(testUser.getName())
                .build();

        // when
        when(editPostService.editPost(eq(testUser.getId()), any(EditPostRequest.class)))
                .thenReturn(expectedResponse);

        // then
        mockMvc.perform(post("/api/posts/edit")
                        .with(oauth2Login().oauth2User(customOAuth2User))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value("새 게시물"))
                .andExpect(jsonPath("$.result.content").value("새 게시물 내용"))
                .andExpect(jsonPath("$.result.authorName").value(testUser.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("POST /api/posts/edit - 기존 게시물 수정")
    void updateExistingPost() throws Exception {
        // given
        EditPostRequest request = EditPostRequest.builder()
                .postId(1L)
                .title("수정된 게시물")
                .content("수정된 내용")
                .build();

        PostDetailResponse expectedResponse = PostDetailResponse.builder()
                .id(1L)
                .title("수정된 게시물")
                .content("수정된 내용")
                .createAt(LocalDateTime.now())
                .authorName(testUser.getName())
                .build();

        // when
        when(editPostService.editPost(eq(testUser.getId()), any(EditPostRequest.class)))
                .thenReturn(expectedResponse);

        // then
        mockMvc.perform(post("/api/posts/edit")
                        .with(oauth2Login().oauth2User(customOAuth2User))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value("수정된 게시물"))
                .andExpect(jsonPath("$.result.content").value("수정된 내용"))
                .andExpect(jsonPath("$.result.authorName").value(testUser.getName()))
                .andDo(print());
    }
}