package se.sowl.postHubingapi.post.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.postHubingapi.fixture.PostFixture;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.dto.EditPostRequest;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EditPostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EditPostService editPostService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    private List<Post> testPosts;

    @BeforeEach
    void setUp(){

        testUser = UserFixture.createUser(null, "테스트1", "테스트유저1", "test1@example.com", "naver");
        testUser = userRepository.save(testUser);

        testPosts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {

            Post post = PostFixture.createPost(null, "테스트 게시물" + i, "테스트 컨텐츠" + i, testUser.getId());
            testPosts.add(post);
        }
        postRepository.saveAll(testPosts);

    }

    @Nested
    @DisplayName("게시판 생성 및 수정")

    class EditPost{
        @Test
        @DisplayName("새 게시물 생성")
        void createNewPost(){
            //given
            EditPostRequest request = new EditPostRequest(null, "새 게시물", "새 내용");

            //when
            PostDetailResponse response = editPostService.editPost(testUser.getId(),request);

            //then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getContent(), response.getContent());

            Post savedPost = postRepository.findById(response.getId()).orElse(null);
            assertNotNull(savedPost);
            assertEquals(request.getTitle(),response.getTitle());
            assertEquals(request.getContent(), response.getContent());
        }
    }

    @Test
    @DisplayName("기존 게시물 수정")
    void updateExistingPost(){
        //given
        Post existingPost = testPosts.get(0);
        EditPostRequest request = new EditPostRequest(existingPost.getId(), "수정된 게시물", "수정된 내용");

        //when
        PostDetailResponse response = editPostService.editPost(testUser.getId(), request);

        //then
        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContent(), response.getContent());
        assertEquals(testUser.getNickname() != null ? testUser.getNickname() : testUser.getName(), response.getAuthorName());

        Post updatedPost = postRepository.findById(existingPost.getId()).orElse(null);
        assertNotNull(updatedPost);
        assertEquals(request.getTitle(), updatedPost.getTitle());
        assertEquals(request.getContent(), updatedPost.getPostContent().getContent());
    }


}