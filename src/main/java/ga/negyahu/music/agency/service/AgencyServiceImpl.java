package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.entity.AgencyRole;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.event.agency.AgencyRegisterEvent;
import ga.negyahu.music.exception.AgencyNotFoundException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyMemberRepository agencyMemberRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Agency register(Agency agency) {

        // 대표 이메일로 계정 1개 생성, 일단은 ACTIVE 가 아님, 관리자 승인 후, 활성화
        String tempPassword = createTempPassword();
        Account save = createAccount(agency, tempPassword);
        agency.setAccount(save);
        // Agency 생성
        agency.setState(State.WAIT);
        Agency register = this.agencyRepository.save(agency);
        // 임시패스워드 발송 -> EventListener
        eventPublisher.publishEvent(new AgencyRegisterEvent(register, tempPassword));
        return register;
    }

    @NotNull
    private Account createAccount(Agency agency, String tempPassword) {
        Account account = agency.getAccount();
        account.setRole(Role.AGENCY);
        account.setState(State.WAIT);
        account.setMobile(agency.getMobile());
        account.setUsername(agency.getBossName());
        account.setPassword(this.passwordEncoder.encode(tempPassword));
        Account save = accountRepository.save(account);
        return save;
    }

    /*
     * 6자리 숫자 임시비밀번호 생성
     * */
    private String createTempPassword() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Agency fetchOwner(Long agencyId) {
        return findByIdElseThrow(agencyId);
    }

    @Override
    public Agency fetchOwner(Long agencyId, Account accountId) {
        return null;
    }

    @Override
    public Integer addManagers(Long id, Account user, String[] emails) {
        Agency agency = findByIdElseThrow(id);
        checkOwner(agency, user);

        List<Account> accounts = this.accountRepository.findAllByEmailIn(emails);
        Set<AgencyMember> agencyMembers = new HashSet<>();
        for (Account account : accounts) {
            AgencyMember manager = AgencyMember.builder()
                .agencyRole(AgencyRole.MANAGER)
                .agency(agency)
                .account(account)
                .build();
            agencyMembers.add(manager);
        }
        List<AgencyMember> results = this.agencyMemberRepository.saveAll(agencyMembers);
        return results.size();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isManager(Long id, Long agencyMemberId) {
        return this.agencyMemberRepository.existsByAgency_IdAndAccount_Id(id, agencyMemberId);
    }

    @Override
    public void permit(Account admin, Long id) {
        if (admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.}");
        }
        Agency agency = findByIdElseThrow(id);
        agency.setState(State.ACTIVE);
        Account boss = agency.getAccount();
        boss.setState(State.ACTIVE);
    }


    private void checkOwner(Agency agency, Account account) {
        if (!agency.isOwner(account)) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
        }
    }

    private Agency findByIdElseThrow(Long id) {
        return agencyRepository.findById(id)
            .orElseThrow(() -> {
                throw new AgencyNotFoundException();
            });
    }

}
