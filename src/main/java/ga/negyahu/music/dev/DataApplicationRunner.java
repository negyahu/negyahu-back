package ga.negyahu.music.dev;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev","prod"})
@Component
@RequiredArgsConstructor
public class DataApplicationRunner implements ApplicationRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!isAction()){
            return;
        }

        Account test1 = Account.builder()
            .username("양우정")
            .email("test1@naver.com")
            .mobile("01011112222")
            .password("dnwjd123")
            .nickname("kingyouzheng")
            .certifiedEmail(true)
            .build();

        accountRepository.save(test1);
    }

    private boolean isAction(){
        return accountRepository.count() == 0;
    }
}
