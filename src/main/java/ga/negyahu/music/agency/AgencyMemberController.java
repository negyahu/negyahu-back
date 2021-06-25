package ga.negyahu.music.agency;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyMemberDto;
import ga.negyahu.music.agency.service.AgencyMemberFetchService;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AgencyMemberController {

    private final AgencyMemberFetchService  agencyMemberFetchService;
    private final AgencyService agencyService;

    @OnlyAdmin
    @GetMapping("/api/agencies/members")
    public ResponseEntity fetchList(@LoginUser Account user, @PageableDefault Pageable pageable){
        Page<AgencyMemberDto> page = agencyMemberFetchService.fetchList(user,pageable);
        return ResponseEntity.ok(page);
    }


}
