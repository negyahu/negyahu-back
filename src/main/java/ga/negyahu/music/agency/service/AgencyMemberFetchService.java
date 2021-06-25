package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyMemberFetchService {

    Page<AgencyMemberDto> fetchList(Account user, Pageable pageable);
}
