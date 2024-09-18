package se.sowl.postHubingapi.post.exception;

import org.springframework.http.HttpStatus;

public class PostException extends RuntimeException{
    private final HttpStatus status;

    public PostException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus(){return status;}


    public static class PostNotFoundException extends PostException {
        public PostNotFoundException() {
            super("존재하지 않는 게시물입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
