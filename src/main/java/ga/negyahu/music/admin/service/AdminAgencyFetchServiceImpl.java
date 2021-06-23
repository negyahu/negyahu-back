package ga.negyahu.music.admin.service;

import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("adminAgencyFetchServiceImpl")
@RequiredArgsConstructor
public class AdminAgencyFetchServiceImpl implements AdminAgencyFetchService {

    private final AgencyRepository agencyRepository;
    @Qualifier("licenseUploadService")
    private final FileUploadService licenseUploadService;

    @Override
    public Page<AgencyDto> fetchAsPageDto(AgencySearch search, Pageable pageable) {
        return this.agencyRepository.searchAgency(search, pageable);
    }

    @Override
    public AgencyDto fetchOne(Long agencyId) {
        return null;
    }

    @Override
    public AgencyUpload fetchImage(Long agencyId) {
        return licenseUploadService.getFileUploadByOwnerId(agencyId);
    }


}
