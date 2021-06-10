package ga.negyahu.music.mail;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.config.MailConfig;
import ga.negyahu.music.mail.template.SignUpMail;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MailConfig.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@Profile("mail-test")
public class MailServiceImplTest {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    private MailService mailService;

    @BeforeEach
    public void init() {
        this.mailService = new MailServiceImpl(this.javaMailSender, templateEngine);
    }

    @Description("메세지 발송 테스트 직접 실행하자. 성공적")
    @Disabled
    @Test
    public void 메일발송_테스트() throws MessagingException {

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
            StandardCharsets.UTF_8.name());

        helper.setTo("yangfriendship@naver.com");
        helper.setSubject("테스트서브젝트");

        Context context = new Context();
        context.setVariable("email", "yangfriendship@naver.com");
        context.setVariable("username", "양우정");

        javaMailSender.send(mimeMessage);
    }

    @Disabled
    @Test
    public void HTML_템플릿_테스트() {
        Context context = new Context();
        String targetName = "양우정";
        String targetEmail = "yangfriendship@naver.com";
        context.setVariable("username", targetName);
        context.setVariable("email", targetEmail);
        String process = templateEngine.process("mail", context);

        Assertions.assertTrue(process.contains(targetName));
    }

    @Test
    @Disabled
    public void HTML_템플릿_메일발송_테스트() throws MessagingException {

        Account account = Account.builder()
            .email("yangfriendship@naver.com")
            .username("양우정")
            .build();

        SignUpMail signUpMail = new SignUpMail(account, "");
        this.mailService.send(signUpMail);
    }

}