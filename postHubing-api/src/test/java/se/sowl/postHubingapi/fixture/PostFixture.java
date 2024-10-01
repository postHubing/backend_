package se.sowl.postHubingapi.fixture;

import org.springframework.test.util.ReflectionTestUtils;
import se.sowl.postHubingdomain.post.domain.Post;
import se.sowl.postHubingdomain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class PostFixture {
    public static Post createPost(Long id, String title, String content, User author){
        Post post = Post.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();

        if (id != null){
            ReflectionTestUtils.setField(post, "id", id);
        }
        return post;
    }

    public static List<Post> createPostList(int count, User author){
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < count; i++){
            posts.add(createPost((long)i + 1, "testPost" + (i + 1), "content" + (i + 1), author));
        }
        return posts;
    }
}
