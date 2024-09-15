package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.service.PostCommentService;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostComment;

import java.util.List;

@RestController
@RequestMapping("/api/postcomments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/list/{postId}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<PostComment>> getCommentsByPostId(@PathVariable("postId") Long postId){
           List<PostComment> postCommentList = postCommentService.getCommentsByPostId(postId);
           return CommonResponse.ok(postCommentList);
    }
}
