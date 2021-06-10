package ga.negyahu.music.utils.annotation;

import ga.negyahu.music.config.SwaggerConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;
import springfox.boot.starter.autoconfigure.SpringfoxConfigurationProperties;
import springfox.boot.starter.autoconfigure.SwaggerUiWebFluxConfiguration;
import springfox.boot.starter.autoconfigure.SwaggerUiWebMvcConfiguration;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@EnableAutoConfiguration(
        exclude = {
            OpenApiAutoConfiguration.class,
            UserDetailsServiceAutoConfiguration.class,  //  507
            ThymeleafAutoConfiguration.class  //487
            }
)
public @interface CustomSpringBootTest {

}
