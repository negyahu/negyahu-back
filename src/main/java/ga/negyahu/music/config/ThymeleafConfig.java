package ga.negyahu.music.config;

import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/*
* REST API 서버라서 따로 ViewRendering 을 위한 TemplateEngine 이 필요하지 않기 때문에,
* 이메일 템플릿을 위한 최소한의 Thymeleaf 을 등록
* */
@Configuration
public class ThymeleafConfig {

    // TODO

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        System.out.println("@@@@@");
        return templateEngine;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        System.out.println("@@@@@");

        return templateResolver;
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasename("mail/MailMessages");
        System.out.println("@@@@@");

        return messageSource;
    }

}
