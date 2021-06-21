package ga.negyahu.music.artist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.dto.ArtistCreateDto;
import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.service.ArtistService;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import ga.negyahu.music.fileupload.service.ArtistFileUploadService;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import ga.negyahu.music.mapstruct.artist.ArtistMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ArtistController {

    public static final String ROOT_URL = "/api/agencies/{agencyId}/artists";
    private ArtistMapper artistMapper = ArtistMapper.INSTANCE;
    private final ArtistFileUploadService artistFileUploadService;
    private final ArtistService artistService;
    private final ArtistFileUploadRepository artistFileUploadRepository;

    @GetMapping("/api/artists")
    public ResponseEntity fetchList(@RequestParam(required = false) ScrollPageable pageable) {
        List<Artist> artists = artistService.fetchList(pageable);
        List<ArtistDto> results = new ArrayList<>(artists.size());
        for(Artist artist :artists) {
            ArtistDto dto = this.artistMapper.toDto(artist);
            results.add(dto);
        }

        return ResponseEntity.ok(results);
    }

    @PostMapping(value = ROOT_URL)
    public ResponseEntity register(@RequestBody ArtistCreateDto createDto,
        @PathVariable("agencyId") Long agencyId, @LoginUser Account user) {
        Artist from = artistMapper.from(createDto);
        Artist register = this.artistService.register(from, agencyId, user);
//        this.artistFileUploadService.setOwner(createDto.getImageId(), register);
        ArtistDto artistDto = this.artistMapper.toDto(register);

        return ResponseEntity.created(URI.create("ddd")).body(artistDto);
    }

    @PostMapping("/api/artists/upload")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file)
        throws IOException {
        // 이미지 파일 체크
        if (!isImageType(file)) {
            return ResponseEntity.badRequest()
                .body(ResultMessage.createFailMessage("[ERROR] 이미지 파일만 업로드할 수 있습니다.").builder());
        }
        // Id 값만 갖고 있는 Artist 객체(FK)와 넘겨받은 Image 를 업로드
        ArtistFileUpload upload = artistFileUploadService
            .saveFile(file, null);

        // 사진의 FullPath 를 Location 에 담아서 반환함 : 201 create
        URI uri = linkTo(methodOn(ArtistController.class).loadImage(upload.getFileName())).toUri();
        return ResponseEntity.created(uri).body(upload);
    }

    @GetMapping("/api/artists/upload/{fileName}")
    public ResponseEntity loadImage(@PathVariable String fileName) throws IOException {

        File file = this.artistFileUploadService.getFileByFileName(fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        IOUtils.toByteArray(file.toURI());
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(file.toURI()));
    }

    private boolean isImageType(MultipartFile file) {
        String type = file.getContentType();
        return type.equals(MediaType.IMAGE_PNG_VALUE) || type.equals(MediaType.IMAGE_JPEG_VALUE);
    }

}
