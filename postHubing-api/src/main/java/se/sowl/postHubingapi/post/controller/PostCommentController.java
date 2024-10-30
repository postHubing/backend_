package se.sowl.postHubingapi.post.controller;

import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingapi.response.PostCommentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/postComments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/list")
    public CommonResponse<List<PostCommentResponse>> getCommentsByPostId(@RequestParam("postId") Long postId){
           List<PostCommentResponse> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }


    @PostMapping("/create")
    public CommonResponse<PostCommentResponse> createComment(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId,
            @RequestBody String content) {
        PostCommentResponse createdComment = postCommentService.createComment(postId, content, userId);
        return CommonResponse.ok(createdComment);
    }


    @PostMapping("/delete")
    public CommonResponse<PostCommentResponse> deleteComment(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId) {
        PostCommentResponse deletedComment = postCommentService.deleteComment(postId,userId);
        return CommonResponse.ok(deletedComment);
    }
}
