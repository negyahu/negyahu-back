package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

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

    @Override
    public Account update(Account account) {
        Account fetch = fetch(account.getId());
        if(account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        modelMapper.map(account,fetch);
        return fetch;
    }
}
