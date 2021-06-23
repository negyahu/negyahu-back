package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.event.account.SignUpEvent;
import ga.negyahu.music.mapstruct.AccountMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountServiceImplTest {

//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    ApplicationEventPublisher eventPublisher;

    @Test
    public void signUpTest(Account account) {
//        String encode = passwordEncoder.encode(account.getPassword());
//        account.setPassword(encode);
//        generateCode(account);
//
//        Account save = this.accountRepository.save(account);
//        eventPublisher.publishEvent(new SignUpEvent(save));
    }

    public void 회원가입_회원중복() {

    }

}
