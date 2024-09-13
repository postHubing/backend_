package se.sowl.postHubingapi.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;

import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    // 둘다 같은 내용의 메소드인데 하나 지우고
    // 이왕이면 getPostList()로 통일하자
    public List<Post> getPostList() { // 모드 게시물 조회
        return postRepository.findAll();
    }


    public List<Post> getAllPosts() {return postRepository.findAll();}
}
