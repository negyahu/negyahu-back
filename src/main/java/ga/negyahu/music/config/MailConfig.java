package ga.negyahu.music.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Import(ThymeleafConfig.class)
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.properties.mail.smtp.timeout}")
    private String mailSmtpTimout;

    @Value("${mail.properties.mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setUsername(this.username);
        sender.setPassword(this.password);
        sender.setPort(this.port);
        sender.setHost(this.host);

        final Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", mailSmtpAuth);
        properties.setProperty("mail.smtp.timeout", mailSmtpAuth);
        properties.setProperty("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);

        sender.setJavaMailProperties(properties);
        return  sender;
    }

}
