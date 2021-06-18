package ga.negyahu.music.admin;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import io.swagger.annotations.ApiOperation;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@OnlyAdmin
public class AdminAgencyController {

    public static final String ROOT_URL = "/api/admin/agencies";
    public static final String PERMIT_URL = ROOT_URL + "/{agencyId}/permit";
    private final AgencyRepository agencyRepository;
    private final AgencyService agencyService;

    @GetMapping(ROOT_URL)
    public ResponseEntity fetchList(@LoginUser Account admin, Pageable pageable,
        AgencySearch search) {
        Page<AgencyDto> agencyDtos = agencyRepository.searchAgency(search, pageable);
        return ResponseEntity.ok(agencyDtos);
    }

    @PatchMapping(PERMIT_URL)
    public ResponseEntity permitAgency(@LoginUser Account admin,
        @PathVariable("agencyId") Long id) {
        agencyService.permit(admin, id);
        return ResponseEntity.noContent().build();
    }


}
