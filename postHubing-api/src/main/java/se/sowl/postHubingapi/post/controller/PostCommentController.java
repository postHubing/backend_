package se.sowl.postHubingapi.post.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.dto.PostCommentRequest;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingapi.response.PostCommentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/postComments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/list")
    public CommonResponse<List<PostCommentResponse>> getCommentsByPostId
            (@RequestParam("postId") Long postId) {
        List<PostCommentResponse> postCommentList = postCommentService.getCommentsByPostId(postId);
        return CommonResponse.ok(postCommentList);
    }

    @PostMapping("/create")
    public CommonResponse<PostCommentResponse> createComment(@RequestBody PostCommentRequest request) {
        PostCommentResponse createdComment = postCommentService.createComment(request);
        return CommonResponse.ok(createdComment);
    }

    @PostMapping("/delete")
    public CommonResponse<PostCommentResponse> deleteComment(@RequestBody PostCommentRequest request) {
        PostCommentResponse deletedComment = postCommentService.deleteComment(request);
        return CommonResponse.ok(deletedComment);
    }
}