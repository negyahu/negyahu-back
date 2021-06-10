package ga.negyahu.music.mail;

import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    public void send(ReceiverContext receiverContext) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        Context ctx = receiverContext.getContext();

        receiverContext.setReceiver(helper);
        String content = thymeleafTemplateEngine.process(receiverContext.getTemplatePath(), ctx);
        helper.setText(content, true);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.info("발송 실패");
            throw e;
        }
    }
}
