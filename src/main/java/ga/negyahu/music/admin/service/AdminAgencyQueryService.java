package ga.negyahu.music.admin.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminAgencyQueryService {

    void changeState(Long agencyId, State state);

}
