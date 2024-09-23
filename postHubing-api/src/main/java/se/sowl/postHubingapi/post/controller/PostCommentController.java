package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingdomain.post.domain.PostComment;

import java.util.List;

@RestController
@RequestMapping("/api/postComments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/list")
    // @RequestParam("postId") -> @RequestParam Long postId
    // 왜 ("postId")를 사용했는지에 대한 이유와
    // Long postId를 사용했을 때의 장단점에 대해 공부 및 설명
    public CommonResponse<List<PostComment>> getCommentsByPostId(@RequestParam("postId") Long postId){
           List<PostComment> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }
}
