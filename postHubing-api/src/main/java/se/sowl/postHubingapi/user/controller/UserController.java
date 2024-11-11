package se.sowl.postHubingapi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.user.dto.request.EditUserRequest;
import se.sowl.postHubingapi.user.dto.response.UserResponse;
import se.sowl.postHubingapi.user.service.UserService;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<UserResponse> getMe(@AuthenticationPrincipal CustomOAuth2User user) {
        User getMeUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return CommonResponse.ok(UserResponse.from(getMeUser));
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<Void> editUser(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody EditUserRequest request) {
        userService.editUser(user.getUserId(), request);
        return CommonResponse.ok();
    }
}
