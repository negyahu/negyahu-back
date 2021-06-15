package ga.negyahu.music.agency;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.mapstruct.AgencyMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/agencies")
@RequiredArgsConstructor
public class AgencyController {

    private AgencyService agencyService;
    private AgencyMapper mapper = AgencyMapper.INSTANCE;

    /*
     * 등록신청, 관리자가 확인후 수락해야만 이용할 수 있다.
     * */
    @PostMapping
    public ResponseEntity register(@RequestBody AgencyCreateDto createDto) {
        Agency agency = mapper.from(createDto);
        agencyService.register(agency);

        URI uri = WebMvcLinkBuilder.linkTo(AgencyController.class).slash(agency.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /*
     * 소속사 개별조회
     * */
    @GetMapping("/{agencyId}")
    public ResponseEntity fetch(@LoginUser Account user,@PathVariable("agencyId")Long agencyId) {
        Agency agency = agencyService.fetch(agencyId);
        Account account = agency.getAccount();
        return null;
    }


    /*
     * Artist
     *
     * */
    /*
     * 소속사에 소속된 모든 아티스트 조회
     * */
    @GetMapping("/{agencyId}/artist")
    public ResponseEntity fetchBySearch(@PathVariable("agencyId") Long id,
        @PageableDefault Pageable pageable) {

        return null;
    }

}
