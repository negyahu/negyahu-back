package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Account signUp(Account account) {
        String encode = passwordEncoder.encode(account.getPassword());
        account.setPassword(encode);
        return this.accountRepository.save(account);
    }

    @Override
    public Account fetch(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            throw new AccountNotFoundException();
        });
    }
}
