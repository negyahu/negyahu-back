package ga.negyahu.music.exception.handler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.exception.AreaNotFountException;
import ga.negyahu.music.exception.ArtistNotFoundException;
import ga.negyahu.music.exception.BadMessageRequestException;
import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.exception.MessageAlreadyOpenException;
import ga.negyahu.music.exception.MessageNotFoundException;
import ga.negyahu.music.exception.Result;
import ga.negyahu.music.exception.ResultMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
public class BaseExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ResultMessage> accountNotFoundExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = AreaNotFountException.class)
    public ResponseEntity<ResultMessage> areaNotFountExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = MessageNotFoundException.class)
    public ResponseEntity<ResultMessage> messageNotFoundExceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = MessageAlreadyOpenException.class)
    public ResponseEntity<ResultMessage> messageAlreadyOpenExceptionHandler(
        MessageAlreadyOpenException e) {
        ResultMessage message = ResultMessage.createFailMessage(e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = BadMessageRequestException.class)
    public ResponseEntity<ResultMessage> badMessageRequestExceptionHandler(
        BadMessageRequestException e) {
        ResultMessage message = ResultMessage.builder()
            .message(e.getMessage())
            .result(Result.FAIL)
            .build();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResultMessage> accessDeniedExceptionHandler(AccessDeniedException e) {
        ResultMessage message = ResultMessage.builder()
            .message(e.getMessage())
            .result(Result.FAIL)
            .build();
        return ResponseEntity.status(FORBIDDEN).body(message);
    }

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<ResultMessage> fileUploadExceptionHandler(FileUploadException e) {
        ResultMessage failMessage = ResultMessage.createFailMessage(e.getMessage());
        return ResponseEntity.badRequest().body(failMessage);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<ResultMessage> fileNotFoundExceptionExceptionHandler(
        FileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = ArtistNotFoundException.class)
    public ResponseEntity<ResultMessage> artistNotFoundExceptionExceptionHandler(
        ArtistNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
