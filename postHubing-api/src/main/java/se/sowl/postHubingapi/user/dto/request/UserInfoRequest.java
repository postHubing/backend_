package se.sowl.postHubingapi.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class UserInfoRequest {
    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private String provider;
}