package ga.negyahu.music.agency;

import static ga.negyahu.music.agency.AgencyController.ROOT_URL;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.ManagerDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.mapstruct.AgencyMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation(value = "/api/agency", tags = "Agency API")
@OpenAPIDefinition()
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
    @ApiOperation(value = "소속사 등록 신청", notes = "관리자 승인 후, 정상이용 가능,등록한 이메일로 계정 생성, 임시비밀번호 발송된다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "소속사 신청 성공"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = AgencyDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "소속사 신청 실패"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @ResponseStatus(value = HttpStatus.CREATED)
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
     * Agency Member
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

    @GetMapping("/{agencyId}/manager")
    public ResponseEntity fetchMembers(@PathVariable("agencyId") Long id,
        @PageableDefault Pageable pageable, @LoginUser Account user) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{agencyId}/manager/{agencyMemberId}")
    public ResponseEntity fetchMember(@PathVariable("agencyId") Long id,
        @PathVariable("agencyMemberId") Long agencyMemberId, @LoginUser Account user) {

        boolean result = this.agencyService.isManager(id, agencyMemberId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{agencyId}/manager")
    public ResponseEntity update(@PathVariable("agencyId") Long id,
        @PageableDefault Pageable pageable, @LoginUser Account user) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{agencyId}/manager")
    public ResponseEntity deleteMember(@PathVariable("agencyId") Long id,
        @PageableDefault Pageable pageable, @LoginUser Account user) {

        return ResponseEntity.ok().build();
    }

}
