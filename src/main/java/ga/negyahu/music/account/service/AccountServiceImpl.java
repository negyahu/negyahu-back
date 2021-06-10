package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.event.SignUpEvent;
import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.mapstruct.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private AccountMapper accountMapper = AccountMapper.INSTANCE;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Transactional
    @Override
    public Account signUp(Account account) {
        String encode = passwordEncoder.encode(account.getPassword());
        account.setPassword(encode);
        Account save = this.accountRepository.save(account);
        eventPublisher.publishEvent(new SignUpEvent(save));
        return save;
    }

    @Override
    public Account fetch(Long id) {
        return accountRepository.findFirstByIdAndStateIsNot(id, State.DELETED).orElseThrow(() -> {
            throw new AccountNotFoundException();
        });
    }

    @Override
    public Account update(Account account) {
        Account fetch = fetch(account.getId());
        if(account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        accountMapper.map(account,fetch);
        return fetch;
    }

    @Override
    public void delete(Long id) {
        Account account = this.fetch(id);
        account.setState(State.DELETED);
    }
}
