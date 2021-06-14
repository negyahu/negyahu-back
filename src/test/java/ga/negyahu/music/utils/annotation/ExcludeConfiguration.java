package ga.negyahu.music.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import springfox.boot.starter.autoconfigure.OpenApiAutoConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAutoConfiguration(
    exclude = {
        OpenApiAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        ThymeleafAutoConfiguration.class
    }
)
public @interface ExcludeConfiguration {

}
