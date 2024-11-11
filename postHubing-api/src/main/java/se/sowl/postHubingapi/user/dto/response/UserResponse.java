package se.sowl.postHubingapi.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import se.sowl.postHubingdomain.user.domain.User;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String provider;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .provider(user.getProvider())
                .build();
    }
}