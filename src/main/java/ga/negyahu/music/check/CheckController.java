package ga.negyahu.music.check;

import ga.negyahu.music.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckController {

    private final AccountRepository accountRepository;

    @GetMapping("/api/check/email")
    public ResponseEntity checkDuplicateEmail(@RequestParam("email")String email){
        boolean exists = this.accountRepository.existsByEmail(email);
        return sendResult(exists);
    }

    @GetMapping("/api/check/nickname")
    public ResponseEntity checkDuplicateNickname(@RequestParam("nickname")String nickname){

        boolean exists = this.accountRepository.existsByNickname(nickname);
        return sendResult(exists);
    }

    private ResponseEntity sendResult(boolean exists) {
        if (exists) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
