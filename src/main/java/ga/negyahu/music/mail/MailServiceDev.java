package ga.negyahu.music.mail;

import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/*
 * MailService 하나로 회원가입, 상품구매 내역 이메일 공통으로 사용하기 위해서
 * Argument
 * */
@Profile("dev")
@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceDev implements MailService {

    @Override
    public void send(ReceiverContext receiverContext) throws MessagingException {

        log.info("MailService is working!!");
    }
}
