package ga.negyahu.music.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;

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
