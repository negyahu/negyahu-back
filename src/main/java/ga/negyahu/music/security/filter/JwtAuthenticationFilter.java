package ga.negyahu.music.security.filter;

import ga.negyahu.music.security.utils.JwtTokenProvider;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(Objects.nonNull(jwt) && jwtTokenProvider.validate(jwt)){
            Authentication authentication = jwtTokenProvider.resolveToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
