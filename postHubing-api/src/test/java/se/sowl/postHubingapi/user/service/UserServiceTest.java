package se.sowl.postHubingapi.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.post.exception.UserException;
import se.sowl.postHubingapi.user.dto.request.EditUserRequest;
import se.sowl.postHubingapi.user.dto.response.UserResponse;
import se.sowl.postHubingdomain.user.InvalidNicknameException;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private OAuthService oAuthService;


    private User testUser;
    private CustomOAuth2User customOAuth2User;

    @BeforeEach
    void setUp() {
        testUser = UserFixture.createUser(1L, "테스트1", "테스트유저1", "test@example.com", "naver");
        testUser = userRepository.save(testUser);
        customOAuth2User = UserFixture.createCustomOAuth2User(testUser);
        when(oAuthService.loadUser(any())).thenReturn(customOAuth2User);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("닉네임 변경")
    class EditUser {
        @Test
        @DisplayName("유저의 2자 이상, 15자 이하의 닉네임이 들어오면 유저 정보를 갱신한다.")
        void setUserNickname() {

            // given
            String newNickname = "안녕2";
            EditUserRequest editUserRequest = new EditUserRequest(newNickname);

            // when
            userService.editUser(testUser.getId(), editUserRequest);

            // then
            User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();

            assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("유저의 새로운 닉네임이 15자 이상인 경우 에러를 표출해야 한다.")
        void setUserNicknameErr() {

            // given
            String newNickname = "매우매우긴닉네임인데감당이가능할까요과연이게진짜과연";
            EditUserRequest editUserRequest = new EditUserRequest(newNickname);

            // when & then
            assertThrows(InvalidNicknameException.class, () -> {
                userService.editUser(testUser.getId(), editUserRequest);
            });
        }
    }

    @Nested
    @DisplayName("유저 조회")
    class GetUser{
        @Test
        @DisplayName("유저의 id를 받아서 해당 유저의 정보를 반환한다.")
        void getUser(){
            //when
            UserResponse userResponse = userService.getUser(testUser.getId());

            //then
            assertThat(userResponse.getId()).isEqualTo(testUser.getId());

        }

        @Test
        @DisplayName("유저의 id가 존재하지 않는 경우 예외를 발생시킨다.")
        void getUserNotFound(){
            //given
            Long nonExistentUserId = 999999L;

            //when & then
            assertThrows(UserException.UserNotFoundException.class, () -> {
                userService.getUser(nonExistentUserId);
            });
        }
    }
}
