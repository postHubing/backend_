package se.sowl.postHubingapi.post.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{
    private final HttpStatus status;

    public UserException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class UserNotFoundException extends  UserException{
        public UserNotFoundException() {
            super("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
