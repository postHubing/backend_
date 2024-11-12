package se.sowl.postHubingapi.post.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.sowl.postHubingapi.fixture.PostFixture;
import se.sowl.postHubingapi.fixture.UserFixture;
import se.sowl.postHubingapi.post.dto.UserRequest;
import se.sowl.postHubingapi.post.exception.PostException;
import se.sowl.postHubingapi.response.PostDetailResponse;
import se.sowl.postHubingapi.response.PostListResponse;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.repository.PostRepository;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

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
    @DisplayName("게시판 조회")

    class getPost{

        @Test
        @DisplayName("모든 게시판 조회")
        void getAllPost(){
            //given

            //when
            List<PostListResponse> postListResponses = postService.getPostList();

            //then
            assertEquals(5, postListResponses.size(),"저장된 게시물은 5개이여야합니다.");
        }

    }

    @Nested
    @DisplayName("게시물 상세 조회")
    class getPostDetail{
        @Test
        @DisplayName("게시물 상세 조회")
        void getTestPostDetail(){
            //given
            Post testPost = testPosts.get(0);

            //when
            PostDetailResponse response = postService.getPostDetail(testPost.getId());

            //then
            assertEquals(testPost.getTitle(), response.getTitle(), "게시물 제목이 일치해야합니다.");
            assertEquals(testPost.getPostContent().getContent(), response.getContent(), "게시물 내용이 일치해야합니다.");
            assertEquals(testUser.getNickname(), response.getAuthorName(), "게시물 작성자 닉네임이 일치해야합니다.");
        }

        @Test
        @DisplayName("존재하지 않는 게시물 조회 시 예외 발생")
        void getPostDetailNotFound() {
            // given
            Long nonExistentPostId = 999999L;

            // when & then
            assertThrows(PostException.PostNotFoundException.class,
                    () -> postService.getPostDetail(nonExistentPostId),
                    "존재하지 않는 게시물 조회 시 PostNotFoundException이 발생해야 합니다.");
        }
    }



    @Nested
    @DisplayName("게시물 검색")
    class searchPosts{
        @Test
        @DisplayName("게시물 검색 성공")
        void searchSuccessPosts(){
            //given
            String keyword = "테스트";  // 더 일반적인 검색어로 변경

            //when
            List<PostListResponse> searchResult = postService.searchPosts(keyword);

            //then
            assertAll(
                    () -> assertFalse(searchResult.isEmpty(), "검색 결과가 존재해야 합니다."),
                    () -> assertTrue(searchResult.size() >= 1, "하나 이상의 게시물이 검색되어야 합니다."),
                    () -> assertTrue(searchResult.stream()
                                    .allMatch(post -> post.getTitle().contains(keyword)),
                            "검색된 모든 게시물의 제목에 검색어가 포함되어야 합니다.")
            );
        }

        @Test
        @DisplayName("검색 결과 없음")
        void searchFailPosts(){
            // given
            String keyword = "존재하지않는게시물";

            // when
            List<PostListResponse> results = postService.searchPosts(keyword);

            // then
            assertTrue(results.isEmpty(), "검색 결과가 없어야 합니다.");
        }
    }

    @Nested
    @DisplayName("특정 사용자 게시물 조회")
    class getPostListByUserId{
        @Test
        @DisplayName("본인 게시물 조회")
        void getMyPagePosts(){
            //given
            UserRequest request = UserRequest.builder()
                    .targetUserId(testUser.getId())
                    .loggedInUserId(testUser.getId())
                    .build();

            //when
            List<PostListResponse> myPagePosts = postService.getPostListByUserId(request);

            //then
            assertAll(
                    () -> assertFalse(myPagePosts.isEmpty(), "본인 게시물이 존재해야 합니다."),
                    () -> assertTrue(myPagePosts.stream()
                                    .allMatch(post -> post.getUserId().equals(testUser.getId())),
                            "본인 게시물만 조회되어야 합니다.")
            );
        }

        @Test
        @DisplayName("타인 게시물 조회")
        void getOtherPagePosts(){
            //given
            UserRequest userRequest = UserRequest.builder()
                    .targetUserId(testUser.getId())
                    .loggedInUserId(999999L)
                    .build();
            //when
            List<PostListResponse> otherPagePosts = postService.getPostListByUserId(userRequest);

            //then
            assertAll(
                    () -> assertFalse(otherPagePosts.isEmpty(), "타인 게시물이 존재해야 합니다."),
                    () -> assertTrue(otherPagePosts.stream()
                                    .allMatch(post -> post.getUserId().equals(testUser.getId())),
                            "타인 게시물만 조회되어야 합니다.")
            );
        }
    }

}