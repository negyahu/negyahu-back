package ga.negyahu.music.agency;

import static ga.negyahu.music.agency.AgencyController.ROOT_URL;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.ManagerDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.mapstruct.AgencyMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import java.net.URI;
import java.util.Map;
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
@RequestMapping(value = ROOT_URL)
@RequiredArgsConstructor
public class AgencyController {

    public static final String ROOT_URL = "/api/agencies";

    private final AgencyService agencyService;
    private AgencyMapper mapper = AgencyMapper.INSTANCE;

    /*
     * 등록신청, 관리자가 확인후 수락해야만 이용할 수 있다.
     * */
    @PostMapping
    public ResponseEntity register(@RequestBody AgencyCreateDto createDto) {
        Agency agency = mapper.from(createDto);
        Agency register = agencyService.register(agency);
        AgencyDto dto = mapper.toDto(register);
        URI uri = WebMvcLinkBuilder.linkTo(AgencyController.class).slash(agency.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /*
     * 소속사 개별조회
     * */
    @GetMapping("/{agencyId}")
    public ResponseEntity fetch(@LoginUser Account user, @PathVariable("agencyId") Long agencyId) {
        Agency agency = agencyService.fetchOwner(agencyId);
        Account account = agency.getAccount();
        AgencyDto dto = mapper.toDto(agency);
        return ResponseEntity.ok(dto);
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

    /*
     * 소속사 직원 추가
     */
    @PostMapping("/{agencyId}/manager")
    public ResponseEntity addManagers(@PathVariable("agencyId") Long id,
        @RequestBody ManagerDto managerDto, @LoginUser Account user) {
        String[] emails = managerDto.getEmails();
        //TODO SQLIntegrityConstraintViolationException 동일한 계정을 추가할 발생, Transactional 확인 후 예외처리 필수!
        Integer addedCount = this.agencyService.addManagers(id, user, emails);
        Map<String, Object> result = Map.of("count", addedCount);
        return ResponseEntity.ok().body(result);
    }

}
