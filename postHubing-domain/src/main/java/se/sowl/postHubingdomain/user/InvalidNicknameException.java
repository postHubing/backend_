package se.sowl.postHubingdomain.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidNicknameException extends ResponseStatusException {
    public InvalidNicknameException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}