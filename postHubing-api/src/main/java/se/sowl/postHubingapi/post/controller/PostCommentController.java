package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingdomain.post.domain.PostComment;

import java.util.List;

@RestController
// 카멜 방식으로 postcomments가 아닌 postComments로 변경
@RequestMapping("/api/postComments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/list/{postId}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<PostComment>> getCommentsByPostId(@PathVariable("postId") Long postId){
           List<PostComment> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }

    // pathvariable을 requestparam으로 변경
    // 아래의 코드에 맞게 controllerTest 변경하고
    // 테스트 코드를 통과하도록 수정
    @GetMapping("/list")
    public CommonResponse<List<PostComment>> getCommentsByPostId_(@RequestParam Long postId){
           List<PostComment> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }
}
