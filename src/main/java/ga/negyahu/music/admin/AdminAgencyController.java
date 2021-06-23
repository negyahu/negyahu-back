package ga.negyahu.music.admin;

import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.admin.service.AdminAgencyFetchService;
import ga.negyahu.music.admin.service.AdminAgencyQueryService;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.util.FileUploadUtils;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequiredArgsConstructor
public class AdminAgencyController {

    private final AdminAgencyFetchService fetchService;
    private final AdminAgencyQueryService queryService;

    @GetMapping("/api/admin/agencies")
    public ResponseEntity<Page<AgencyDto>> fetchList(@PageableDefault Pageable pageable,
        @ModelAttribute AgencySearch search) {
        Page<AgencyDto> pages = fetchService.fetchAsPageDto(search, pageable);
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/api/admin/agencies/{agencyId}/license")
    public ResponseEntity<byte[]> loadLicenseImage(@PathVariable Long agencyId) throws IOException {
        AgencyUpload upload = this.fetchService.fetchImage(agencyId);
        File file = FileUploadUtils.getFile(upload);
        return FileUploadUtils.transToByteRes(file);
    }

    @PatchMapping("/api/admin/agencies/{agencyId}")
    public ResponseEntity<Object> changeState(@PathVariable("agencyId") Long agencyId,
        @RequestBody State state) {
        this.queryService.changeState(agencyId, state);
        return ResponseEntity.noContent().build();
    }

}
