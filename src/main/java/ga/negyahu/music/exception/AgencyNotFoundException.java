package ga.negyahu.music.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgencyNotFoundException extends RuntimeException{

    public AgencyNotFoundException() {
        super("[ERROR] 존재하지 않는 소속사입니다.");
    }

    public AgencyNotFoundException(String message) {
        super(message);
    }
}
