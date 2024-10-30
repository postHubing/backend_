package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.post.service.EditPostService;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingdomain.user.domain.CustomOAuth2User;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class EditPostController {

    private final EditPostService editPostService;

    @PostMapping("/edit")
    public CommonResponse<PostDetailResponse> editPost(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody EditPostRequest request
    ){
        PostDetailResponse postDetailResponse = editPostService.editPost(user.getUserId(), request);
        return CommonResponse.ok(postDetailResponse);
    }
}
