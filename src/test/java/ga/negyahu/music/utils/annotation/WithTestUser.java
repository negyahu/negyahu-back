package ga.negyahu.music.utils.annotation;

import ga.negyahu.music.account.entity.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithTestUser {

    long userId() default 0L;

    String userEmail() default "";

    String username() default "";

    String nickname() default "";

    String password() default "";

    Role role() default Role.USER;

    String mobile() default "";

}
