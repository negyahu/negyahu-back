package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService{

    private final AgencyRepository agencyRepository;
    private final AccountRepository accountRepository;

    @Override
    public Agency register(Agency agency) {
        Account ceo = agency.getAccount();
        ceo.setRole(Role.AGENCY);
        return this.agencyRepository.save(agency);
    }

}
