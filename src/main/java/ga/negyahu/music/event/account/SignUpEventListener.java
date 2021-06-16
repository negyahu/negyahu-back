package ga.negyahu.music.event.account;

import static java.util.Objects.isNull;

import ga.negyahu.music.mail.MailService;
import ga.negyahu.music.mail.template.SignUpMail;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"prod"})
public class SignUpEventListener implements ApplicationListener<SignUpEvent> {

    private final MailService mailService;

    @Async
    @Override
    public void onApplicationEvent(SignUpEvent event) {
        SignUpMail signUpMail = event.getSignUpMail();

        if (isNull(signUpMail) || isNull(signUpMail.getAccount())) {
            log.info("메일발송 실패");
        }
        try {
            mailService.send(signUpMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
