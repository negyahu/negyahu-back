package ga.negyahu.music.security.provider;

import ga.negyahu.music.security.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticateProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String rawPassword = (String) authentication.getCredentials();
        String email = (String) authentication.getPrincipal();

        AccountContext context = (AccountContext) userDetailsService
            .loadUserByUsername(email);
        if(!passwordEncoder.matches(rawPassword, context.getPassword())){
            throw new BadCredentialsException(context.getUsername());
        }

        return new UsernamePasswordAuthenticationToken(context,null, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
