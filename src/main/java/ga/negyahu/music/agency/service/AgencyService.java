package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.Agency;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyService {

    Agency register(Agency agency);

    Agency fetch(Long agencyId);

    Agency fetch(Long agencyId, Account accountId);
}
