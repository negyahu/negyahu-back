package ga.negyahu.music.admin.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.event.agency.AgencyRegisterEvent;
import ga.negyahu.music.exception.AgencyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service("adminAgencyQueryServiceImpl")
public class AdminAgencyQueryServiceImpl implements AdminAgencyQueryService {

    private final AgencyRepository agencyRepository;

    @Override
    public void changeState(Long agencyId, State state) {
        Agency agency = fetchAgency(agencyId);
        if (state == State.IGNORE) {
            agency.ignore();
        } else {
            agency.permit();
        }
    }

    private Agency fetchAgency(Long agencyId) {
        return this.agencyRepository.findById(agencyId)
            .orElseThrow(() -> {
                throw new AgencyNotFoundException();
            });
    }

}
