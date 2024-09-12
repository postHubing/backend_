package se.sowl.postHubingapi.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.user.service.UserService;
import se.sowl.postHubingdomain.user.InvalidNicknameException;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @MockBean
    private UserService userService;

    private User testUser;
    private CustomOAuth2User customOAuth2User;

    @BeforeEach
    void setUp() {
        testUser = UserFixture.createUser(1L, "테스트", "테스트유저", "test@example.com", "naver");
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
        when(oAuthService.loadUser(any())).thenReturn(customOAuth2User);
    }

    @Nested
    @DisplayName("PUT /api/users/edit")
    class EditUser {

        @Test
        @DisplayName("유저 정보 수정 성공")
        @WithMockUser(roles = "USER")
        public void editSuccess() throws Exception {
            mockMvc.perform(put("/api/users/edit")
                            .with(oauth2Login().oauth2User(customOAuth2User))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nickname\": \"qwerty\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("SUCCESS"))
                    .andExpect(jsonPath("$.message").value("성공"));
        }

        /*@Test
        @DisplayName("유저 정보 수정 실패 (닉네임 길이 2미만 15이상)")
        @WithMockUser(roles = "USER")
        public void editFailByShortLength() throws Exception {
            doThrow(new InvalidNicknameException("닉네임은 2자 이상 15자 이하여야 합니다."))
                    .when(userService).editUser(eq(testUser.getId()), any());

            mockMvc.perform(put("/api/users/edit")
                            .with(oauth2Login().oauth2User(customOAuth2User))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nickname\": \"a\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("닉네임은 2자 이상 15자 이하여야 합니다."));
        }*/
    }
}