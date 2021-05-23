package ga.negyahu.music.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class BaseExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity accountNotFoundException() {
        ExceptionMessage message = ExceptionMessage.builder()
            .code(400)
            .message("존재하지 않는 회원").build();
        return ResponseEntity.badRequest().body(message);
    }

}
