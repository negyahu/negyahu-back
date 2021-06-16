package ga.negyahu.music.event.agency;

import static java.util.Objects.isNull;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.event.account.SignUpEvent;
import ga.negyahu.music.mail.MailService;
import ga.negyahu.music.mail.template.AgencyRegisterMail;
import ga.negyahu.music.mail.template.SignUpMail;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Profile("prod")
@Component
@RequiredArgsConstructor
@Slf4j
public class AgencyEventListener implements ApplicationListener<AgencyRegisterEvent> {

    private final MailService mailService;

    /*
     * First cache 가 언제 종료될지 모르기 때문에 service layer 에서 password encoding 을 진행하고
     * 따로 rawPassword 를 넘겨받은 후, 이메일 발송을을 한다.
     * */
    @Async
    @Override
    public void onApplicationEvent(AgencyRegisterEvent event) {
        if (!supports(event)) {
            return;
        }
        log.info("AgencyEventListener is working~");
        AgencyRegisterMail mail = event.getMail();
        try {
            this.mailService.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // TODO RAWPWD 를 대표이메일에게 발송한다.
    }

    public boolean supports(Object target) {
        return target.getClass().isAssignableFrom(AgencyRegisterEvent.class);
    }
}
