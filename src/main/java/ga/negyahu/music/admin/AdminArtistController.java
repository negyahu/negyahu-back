package ga.negyahu.music.admin;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.security.annotation.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminArtistController {

    @PatchMapping("/api/admin/artist/{id}")
    public ResponseEntity accept(@PathVariable Long id,@LoginUser Account account) {

        return ResponseEntity.ok().build();
    }

}
