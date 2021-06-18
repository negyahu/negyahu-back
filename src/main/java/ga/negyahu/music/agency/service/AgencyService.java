package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.Agency;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyService {

    Agency register(Agency agency);

    Agency fetchOwner(Long agencyId);

    Agency fetchOwner(Long agencyId, Account accountId);

    Integer addManagers(Long id, Account user, String[] emails);


    boolean isManager(Long id, Long agencyMemberId);

    void permit(Account admin, Long id);
}
