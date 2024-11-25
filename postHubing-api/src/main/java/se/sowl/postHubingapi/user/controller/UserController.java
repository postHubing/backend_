package se.sowl.postHubingapi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.exception.UserException;
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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<UserResponse> getMe(@AuthenticationPrincipal CustomOAuth2User user) {
        UserResponse userResponse = userService.getUser(user.getUserId());
        return CommonResponse.ok(userResponse);
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<Void> editUser(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody EditUserRequest request) {
        userService.editUser(user.getUserId(), request);
        return CommonResponse.ok();
    }

    @GetMapping("/userId")
    public CommonResponse<UserResponse> getUser(@RequestParam("userId") Long userId){
        UserResponse userResponse = userService.getUser(userId);
        return CommonResponse.ok(userResponse);
    }


}
