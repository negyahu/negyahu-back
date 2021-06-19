package ga.negyahu.music.artist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArtistController {

    public static final String ROOT_URL = "/api/artist";
    public static final String ARTIST_FILE_URL = "/api/upload/artist";

//    private final ArtistFileUploadService artistFileUploadService;

    @PostMapping(ROOT_URL)
    public ResponseEntity register() {

        return null;
    }




}