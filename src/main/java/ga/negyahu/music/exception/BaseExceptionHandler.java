package ga.negyahu.music.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class BaseExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity accountNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = AreaNotFountException.class)
    public ResponseEntity areaNotFountExceptionException() {
        return ResponseEntity.notFound().build();
    }

}
