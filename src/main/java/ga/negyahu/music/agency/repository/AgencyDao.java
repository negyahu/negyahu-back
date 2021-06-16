package ga.negyahu.music.agency.repository;

import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgencyDao {

    Page<AgencyDto> searchAgency(AgencySearch search, Pageable pageable);

}
