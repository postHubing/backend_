package se.sowl.postHubingapi.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.postHubingapi.post.exception.UserException;
import se.sowl.postHubingapi.user.dto.request.EditUserRequest;
import se.sowl.postHubingapi.user.dto.response.UserResponse;
import se.sowl.postHubingdomain.user.domain.User;
import se.sowl.postHubingdomain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void editUser(Long userId, EditUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.updateNickname(request.getNickname());
        userRepository.save(user);
    }

    @Transactional
    public UserResponse getUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);
        return UserResponse.from(user);
    }
}
