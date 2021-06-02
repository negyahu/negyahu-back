package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.event.SignUpEvent;
import ga.negyahu.music.event.SignUpEventHandler;
import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.mapstruct.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Profile({"dev","prod"})
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpEventHandler signUpEventHandler;
    private AccountMapper accountMapper = AccountMapper.INSTANCE;

    @Transactional
    @Override
    public Account signUp(Account account) {
        String encode = passwordEncoder.encode(account.getPassword());
        account.setPassword(encode);
        Account save = this.accountRepository.save(account);
        signUpEventHandler.sendSignUpMessage(new SignUpEvent(save));
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
}
