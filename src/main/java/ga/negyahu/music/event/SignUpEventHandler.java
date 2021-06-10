package ga.negyahu.music.event;

import static java.util.Objects.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mail.MailService;
import ga.negyahu.music.mail.MailServiceImpl;
import ga.negyahu.music.mail.template.SignUpMail;
import java.util.Objects;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"prod","dev"})
public class SignUpEventHandler {

    private final MailService mailService;

    @Async
    @EventListener
    public void sendSignUpMessage(SignUpEvent event) {
        SignUpMail signUpMail = event.getSignUpMail();

        if(isNull(signUpMail) || isNull(signUpMail.getAccount())) {
            log.info("메일발송 실패");
        }
        try {
            mailService.send(signUpMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
