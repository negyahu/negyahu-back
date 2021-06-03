package ga.negyahu.music.index;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/api")
    public ResponseEntity home() {
        return ResponseEntity.ok().body("Index page");
    }

}
