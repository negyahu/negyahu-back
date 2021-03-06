package ga.negyahu.music.agency;

import static ga.negyahu.music.agency.AgencyController.ROOT_URL;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencyMeDto;
import ga.negyahu.music.agency.dto.ManagerDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.exception.Result;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.service.FileUploadService;
import ga.negyahu.music.mapstruct.AgencyMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.swagger.annotation.AgencyIDParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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

@ApiOperation(value = "/api/agency", tags = "????????? API")
@RestController
@RequestMapping(value = ROOT_URL)
@RequiredArgsConstructor
public class AgencyController {

    public static final String ROOT_URL = "/api/agencies";

    private final AgencyService agencyService;
    @Qualifier("agencyFileUploadService")
    private final FileUploadService<AgencyUpload> fileUploadService;
    private AgencyMapper mapper = AgencyMapper.INSTANCE;

    /*
     * ????????????, ???????????? ????????? ??????????????? ????????? ??? ??????.
     * */
    @ApiOperation(value = "????????? ?????? ??????", notes = "????????? ?????? ???, ???????????? ??????,????????? ???????????? ?????? ??????, ?????????????????? ????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "????????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = AgencyDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity register(@RequestBody AgencyCreateDto createDto) {
        Agency agency = mapper.from(createDto);
        Agency register = agencyService.register(agency);
        AgencyDto dto = mapper.toDto(register);
        fileUploadService.setOwner(createDto.getFileId(), register);

        URI uri = WebMvcLinkBuilder.linkTo(AgencyController.class).slash(agency.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /*
     * ????????? ????????????
     * */
    @ApiOperation(value = "????????? ?????? ??????", notes = "????????? ?????? ??????")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = AgencyDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @ResponseStatus(value = HttpStatus.OK)
    @AgencyIDParam
    @GetMapping("/{id}")
    public ResponseEntity<AgencyDto> fetch(@LoginUser Account user, @PathVariable("id") Long agencyId) {
        Agency agency = agencyService.fetchOwner(agencyId);
        AgencyDto dto = mapper.toDto(agency);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<AgencyMeDto> fetchMe(@LoginUser Account user){
        AgencyMeDto agencyMeDto = this.agencyService.fetchMe(user);
        return ResponseEntity.ok(agencyMeDto);
    }

    /*
     * ???????????? ????????? ?????? ???????????? ??????
     * */
    @GetMapping("/{agencyId}/artist")
    public ResponseEntity fetchBySearch(@PathVariable("agencyId") Long id,
        @PageableDefault Pageable pageable) {

        return null;
    }

    /*
     * Agency Member
     */
    @ApiOperation(value = "????????? ?????? ??????", notes = "???????????? ????????? ????????? ????????? ???, ????????????. ????????? ????????? ??????????????? ?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "?????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = ManagerDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "?????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @ResponseStatus(value = HttpStatus.OK)
    @AgencyIDParam
    @PostMapping("/{agencyId}/manager")
    public ResponseEntity addManagers(@PathVariable("agencyId") Long id,
        @RequestBody ManagerDto managerDto, @LoginUser Account user) {
        if (user.getRole() != Role.AGENCY) {
            return ResponseEntity.status(403).body(ResultMessage.create403Message());
        }

        String[] emails = managerDto.getEmails();
        //TODO SQLIntegrityConstraintViolationException ????????? ????????? ????????? ??????, Transactional ?????? ??? ???????????? ??????!
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
        if (!result) {
            ResultMessage resultMessage = ResultMessage.builder().message("[ERROR] ????????? ??? ????????????.")
                .result(Result.FAIL).build();
            return ResponseEntity.status(403).body(resultMessage);
        }

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
