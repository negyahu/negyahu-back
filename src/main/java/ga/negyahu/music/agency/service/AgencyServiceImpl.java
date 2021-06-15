package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.exception.AgencyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyMemberRepository agencyMemberRepository;
    private final AccountRepository accountRepository;

    @Override
    public Agency register(Agency agency) {
        Account ceo = agency.getAccount();
        ceo.setRole(Role.AGENCY);
        return this.agencyRepository.save(agency);
    }

    @Override
    public Agency fetch(Long agencyId) {
        return findByIdElseThrow(agencyId);
    }

    @Override
    public Agency fetch(Long agencyId, Account accountId) {
        return null;
    }

    private Agency findByIdElseThrow(Long id) {
        return agencyRepository.findById(id)
            .orElseThrow(() -> {
                throw new AgencyNotFoundException();
            });
    }

}
