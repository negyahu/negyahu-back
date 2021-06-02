package ga.negyahu.music.event;

import ga.negyahu.music.account.Account;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignUpEventHandler {

    @Async
    @EventListener
    public void sendSignUpMessage(SignUpEvent event) {
        Account account = event.getAccount();
        if (Objects.isNull(account.getNickname()) || Objects.isNull(account.getEmail())) {
            return;
        }
      log.info("sendSignUpMessage!!!");
    }

}
