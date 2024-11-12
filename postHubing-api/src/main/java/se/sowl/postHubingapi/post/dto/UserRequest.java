package se.sowl.postHubingapi.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private Long targetUserId;
    private Long loggedInUserId;
}
