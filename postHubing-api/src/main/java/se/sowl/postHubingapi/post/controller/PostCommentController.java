package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
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
    public CommonResponse<List<PostComment>> getCommentsByPostId(@RequestParam("postId") Long postId){
           List<PostComment> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }
}
