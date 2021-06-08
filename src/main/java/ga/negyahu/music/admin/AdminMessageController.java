package ga.negyahu.music.admin;

import ga.negyahu.music.message.dto.MessageSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminMessageController {

    @GetMapping
    public ResponseEntity fetch(MessageSearch search,
        @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok().build();
    }

}
