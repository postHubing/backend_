package se.sowl.postHubingapi.post.exception;

import org.springframework.http.HttpStatus;

public class PostException extends RuntimeException{
    private final HttpStatus status;

    public PostException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public static class PostNotFoundException extends PostException {
        public PostNotFoundException() {
            super("존재하지 않는 게시물입니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class PostNotAuthorizedException extends PostException{
        public PostNotAuthorizedException(){
            super("게시글 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }
    public static class CommentContentTooShortException extends PostException{
        public CommentContentTooShortException(){
            super("댓글 내용은 2자 이상이여야합니다.", HttpStatus.BAD_REQUEST);
        }
    }
    public static class CommentNotFoundException extends PostException{
        public CommentNotFoundException(){
            super("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
