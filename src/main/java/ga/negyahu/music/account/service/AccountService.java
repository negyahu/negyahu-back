package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountService {

    Account signUp(Account account);

    Account fetch(Long id);

    Account update(Account account);

    void delete(Long id);
}
