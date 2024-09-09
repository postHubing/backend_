package se.sowl.postHubingapi.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.oauth.service.OAuthService;
import se.sowl.postHubingapi.user.dto.EditUserRequest;
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
        testUser = UserFixture.createUser(null, "테스트", "테스트유저", "test@example.com", "naver");
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
            User fixtureUser = UserFixture.createUser(1L, "안녕", "안녕1", "dasd@naver.com", "naver");
            User user = userRepository.save(fixtureUser);
            String newNickname = "안녕2";
            EditUserRequest editUserRequest = new EditUserRequest(newNickname);

            // when & then
            userService.editUser(user.getId(), editUserRequest);

            User updatedUser = userRepository.findById(user.getId()).orElseThrow();

            assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("유저의 새로운 닉네임이 15자 이상인 경우 에러를 표출해야 한다.")
        void setUserNicknameErr() {

            // given
            User fixtureUser = UserFixture.createUser(1L, "안녕", "안녕1", "dasd@naver.com", "naver");
            User user = userRepository.save(fixtureUser);
            String newNickname = "매우매우긴닉네임인데감당이가능할까요과연이게진짜과연";
            EditUserRequest editUserRequest = new EditUserRequest(newNickname);

            // when & then
            assertThrows(InvalidNicknameException.class, () -> {
                userService.editUser(user.getId(), editUserRequest);
            });

        }
    }
}
