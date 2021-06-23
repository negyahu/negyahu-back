package ga.negyahu.music.artist;

import static ga.negyahu.music.fileupload.controller.UploadController.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.dto.ArtistCreateDto;
import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import ga.negyahu.music.artist.dto.ArtistMemberDto;
import ga.negyahu.music.artist.dto.ArtistMemberUpdateDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.service.ArtistMemberService;
import ga.negyahu.music.artist.service.ArtistService;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.fileupload.controller.UploadController;
import ga.negyahu.music.fileupload.entity.ArtistUpload;
import ga.negyahu.music.fileupload.entity.ArtistMemberUpload;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import ga.negyahu.music.fileupload.service.ArtistUploadService;
import ga.negyahu.music.fileupload.service.ArtistMemberUploadService;
import ga.negyahu.music.mapstruct.artist.ArtistMapper;
import ga.negyahu.music.mapstruct.artist.ArtistMemberMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private ArtistMemberMapper artistMemberMapper = ArtistMemberMapper.INSTANCE;
    private final ArtistUploadService artistFileUploadService;
    private final ArtistService artistService;
    private final ArtistMemberService artistMemberService;
    private final ArtistFileUploadRepository artistFileUploadRepository;
    private final ArtistMemberUploadService artistMemberUploadService;

    @GetMapping("/api/artists")
    public ResponseEntity fetchList(@RequestParam(required = false) ScrollPageable pageable) {
        List<Artist> artists = artistService.fetchList(pageable);
        List<ArtistDto> results = new ArrayList<>(artists.size());
        for (Artist artist : artists) {
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

    /*
     * Artist 가 등록되기 전에 사용
     * */
    @PostMapping("/api/artists/upload")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file,
        @LoginUser Account user) throws IOException {
        // 이미지 파일 체크
        if (!isImageType(file)) {
            return ResponseEntity.badRequest()
                .body(BadRequestMessage());
        }
        // Id 값만 갖고 있는 Artist 객체(FK)와 넘겨받은 Image 를 업로드
        ArtistUpload upload = artistFileUploadService.saveFile(file, null, user);

        // 사진의 FullPath 를 Location 에 담아서 반환함 : 201 create
        return createResponseEntity(upload);
    }

    @GetMapping("/api/member/{memberId}/upload")
    public ResponseEntity uploadMemberImage(@RequestParam(("file")) MultipartFile file,
        @LoginUser Account user, @PathVariable("memberId") Long memberId) {
        ArtistMemberUpload fileUpload = this.artistMemberUploadService
            .saveFile(file, ArtistMember.getOnlyIdEntity(memberId), user);
        return ResponseEntity.created(null).body(fileUpload);
    }

    @PostMapping("/api/agencies/{agencyId}/artists/{artistId}/members")
    public ResponseEntity createMembers(@RequestBody List<ArtistMemberCreateDto> dtos,
        @LoginUser Account user,
        @PathVariable("agencyId") Long agencyId,
        @PathVariable("artistId") Long artistId) {

        this.artistService.checkIsAdmin(user, agencyId);
        List<ArtistMember> artistMembers = dtos.stream()
            .map(d ->
                this.artistMemberMapper.from(d)
            ).collect(Collectors.toList());

        List<ArtistMember> results = this.artistService
            .createMembers(artistId, artistMembers);

        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/api/artists/{artistId}/members/{memberId}")
    public ResponseEntity fetchMember(@LoginUser Account user,
        @PathVariable("agencyId") Long agencyId,
        @PathVariable("memberId") Long memberId) {

        ArtistMember member = this.artistMemberService.fetch(memberId);
        ArtistMemberDto dto = this.artistMemberMapper.toDto(member);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/api/artists/{artistId}/members")
    public ResponseEntity addMember(@PathVariable("artistId") Long artistId,
        @RequestBody ArtistMemberCreateDto dto, @LoginUser Account user) {
        ArtistMember member = this.artistMemberMapper.from(dto);
        ArtistMember save = this.artistMemberService.register(artistId, member);

        ArtistMemberDto result = this.artistMemberMapper.toDto(save);
        URI uri = linkTo(methodOn(ArtistController.class).fetchMember(null, null, null))
            .toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @PatchMapping("/api/artists/{artistId}/members/{memberId}")
    public ResponseEntity updateMember(@LoginUser Account user,
        @PathVariable("agencyId") Long agencyId, @PathVariable("artistId") Long artistId,
        @PathVariable("memberId") Long memberId, @RequestBody ArtistMemberUpdateDto dto) {
        ArtistMember from = this.artistMemberMapper.from(dto);

        ArtistMember member = this.artistMemberService.update(from);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/api/members/{memberId}/upload")
    public ResponseEntity uploadMemberImage(@RequestParam("file") MultipartFile file
        , @PathVariable("memberId") Long memberId, @LoginUser Account user) throws IOException {
        if (!isImageType(file)) {
            return ResponseEntity.badRequest()
                .body(BadRequestMessage());
        }
        ArtistMember entity = ArtistMember.builder().id(memberId).build();
        ArtistMemberUpload upload = artistMemberUploadService
            .saveFile(file, entity, user);

        return createResponseEntity(upload);
    }

    private ResultMessage BadRequestMessage() {
        return ResultMessage.createFailMessage("[ERROR] 이미지 파일만 업로드할 수 있습니다.");
    }


    private boolean isImageType(MultipartFile file) {
        String type = file.getContentType();
        return type.equals(MediaType.IMAGE_PNG_VALUE) || type.equals(MediaType.IMAGE_JPEG_VALUE);
    }

}
