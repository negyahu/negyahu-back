package ga.negyahu.music.admin.service;

import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAgencyFetchService {

    Page<AgencyDto> fetchAsPageDto(AgencySearch search, Pageable pageable);

    AgencyDto fetchOne(Long agencyId);

    AgencyUpload fetchImage(Long agencyId);
}
