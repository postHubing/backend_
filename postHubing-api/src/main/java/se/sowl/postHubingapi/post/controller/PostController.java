package se.sowl.postHubingapi.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.sowl.postHubingapi.common.CommonResponse;
import se.sowl.postHubingapi.post.service.PostService;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingapi.response.PostListResponse;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<List<PostListResponse>> getPostList(){
        List<PostListResponse> postList = postService.getPostList();
        return CommonResponse.ok(postList);
    }

    @GetMapping("/detail")
    public CommonResponse<PostDetailResponse> getPostDetail(@RequestParam("postId") Long postId){
        PostDetailResponse response = postService.getPostDetail(postId);
        return CommonResponse.ok(response);
    }

    @GetMapping("/search")
    public CommonResponse<List<PostListResponse>> searchPosts(@RequestParam("keyword") String keyword){
        List<PostListResponse> searchResult = postService.searchPosts(keyword);
        return CommonResponse.ok(searchResult);
    }
}


