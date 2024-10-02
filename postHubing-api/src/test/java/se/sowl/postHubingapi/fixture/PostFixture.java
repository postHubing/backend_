package se.sowl.postHubingapi.fixture;

import org.springframework.test.util.ReflectionTestUtils;
import se.sowl.postHubingapi.post.dto.PostDTO;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.post.domain.PostContent;
import se.sowl.postHubingdomain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class PostFixture {

    public static Post createPost(Long id, String title, String content, Long userId){
        Post post = Post.builder()
                .title(title)
                .userId(userId)
                .build();

        if (id != null) {
            ReflectionTestUtils.setField(post, "id", id);
        }
        PostContent postContent = new PostContent(post, content);
        post.setPostContent(postContent);
        return post;
    }

    public static List<Post> createPostList(int count, Long userId){
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < count; i++){
            posts.add(createPost((long)i + 1, "testPost" + (i + 1), "content" + (i + 1), userId));
        }
        return posts;
    }

//    public static List<PostDTO> createPostList(int count, Long userId){
//        List<PostDTO> postList = new ArrayList<>();
//        for (int i = 0; i < count; i++){
//            Post post = createPost((long)i + 1, "testPost" + (i + 1), "content" + (i + 1), userId);
//            postList.add(new PostDTO(post));
//        }
//        return postList;
//    }
}
