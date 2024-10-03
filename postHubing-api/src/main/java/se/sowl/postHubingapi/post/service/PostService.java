package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public List<PostListResponse> getPostList() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostListResponse::from)
                .toList();
    }
}

