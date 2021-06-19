package ga.negyahu.music.admin;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAgencyController {

    private final AgencyRepository agencyRepository;

    @OnlyAdmin
    @GetMapping("/api/admin/agencies")
    public ResponseEntity fetchList(@LoginUser Account admin, @PageableDefault Pageable pageable,
        @RequestParam
            AgencySearch search) {
        Page<AgencyDto> agencyDtos = agencyRepository.searchAgency(search, pageable);
        return ResponseEntity.ok(agencyDtos);
    }

}
