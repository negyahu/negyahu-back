package ga.negyahu.music.agency;

import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    /*
    * 등록신청, 관리자가 확인후 수락해야만 이용할 수 있다.
    * */
    @PostMapping
    public ResponseEntity register(@RequestBody AgencyCreateDto createDto) {
        
        return null;
    }

    /*
     * 소속사 개별조회
     * */
    @GetMapping
    public ResponseEntity fetch() {
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
