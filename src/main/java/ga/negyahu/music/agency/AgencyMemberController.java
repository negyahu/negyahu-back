package ga.negyahu.music.agency;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgencyMemberController {

    private static final String ROOT_URL = "/api/agencies/{agencyId}/members";

    /*
    * 소속사에 등록되어 있는 멤버 조회 페이징
    * */
    @GetMapping(ROOT_URL)
    public ResponseEntity fetchMembers(@PathVariable("agencyId") Long agencyId, @PageableDefault
        Pageable pageable) {
        return null;
    }

    /*
    * 소속사에 등로되어 있는 멤버를 개별조회
    * */
    @GetMapping(ROOT_URL + "/{id}")
    public ResponseEntity fetchMember(@PathVariable("agencyId") Long agencyId,
        @PathVariable Long id) {
        return null;
    }

    /*
     * 등록할 멤버의 ID값을 받아온 후, 등록(수락대기)로 설정후 저장, 메세지 발송
     * */
    @PostMapping(ROOT_URL)
    public ResponseEntity registerMembers() {
        return null;
    }

    /*
     * 회사에 소속된 사람의 신분을 바꾼다. AgencyRole
     * */
    @PatchMapping(ROOT_URL +"/{id}")
    public ResponseEntity modify() {
        return null;
    }

    /*
     * 소속된 멤버를 삭제함
     * */
    @DeleteMapping("/api/agencies/{agencyId}/members/{id}")
    public ResponseEntity delete() {
        return null;
    }


}