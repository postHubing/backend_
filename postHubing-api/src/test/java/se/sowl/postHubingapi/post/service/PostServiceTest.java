package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    private Post postTest;





}