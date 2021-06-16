package ga.negyahu.music.dev;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Component
@RequiredArgsConstructor
public class DataApplicationRunner implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!isAction()) {
            return;
        }

        Account test1 = Account.builder()
            .username("양우정")
            .email("test1@naver.com")
            .mobile("01011112222")
            .password(passwordEncoder.encode("dnwjd123"))
            .nickname("킹우정")
            .certifiedEmail(true)
            .build();
        Account test2 = Account.builder()
            .username("황유정")
            .email("hyu630115@gmail.com")
            .mobile("01022223333")
            .password(passwordEncoder.encode("dbwjd123"))
            .nickname("갓유정")
            .certifiedEmail(true)
            .build();

        accountRepository.save(test1);
        accountRepository.save(test2);
    }

    private boolean isAction() {
        return accountRepository.count() == 0;
    }
}