package ga.negyahu.music.exception;

public class MessageAlreadyOpenException extends RuntimeException{

    public MessageAlreadyOpenException() {
        super("[ERROR] 이미 수신완료된 메세지입니다.");
    }

    public MessageAlreadyOpenException(String message) {
        super(message);
    }
}
