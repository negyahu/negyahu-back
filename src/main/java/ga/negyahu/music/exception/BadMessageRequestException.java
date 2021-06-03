package ga.negyahu.music.exception;

public class BadMessageRequestException extends RuntimeException{

    public BadMessageRequestException() {
        super("[ERROR] 잘못된 메세지 요청입니다.");
    }

    public BadMessageRequestException(String message) {
        super(message);
    }
}
