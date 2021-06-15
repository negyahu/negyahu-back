package ga.negyahu.music.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.security.filter.JwtAuthenticationFilter;
import ga.negyahu.music.security.filter.JwtLoginProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String LOGIN_URL = "/api/login";

    private final JwtAuthenticationFilter jwtFilter;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManagerBean;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
//    private final CorsConfigurationSource corsConfigurationSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/api/admin/*").authenticated()
            .antMatchers(HttpMethod.GET, "/api/*").permitAll()
            .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
            .anyRequest().authenticated()
        ;

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtLoginProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtLoginProcessingFilter jwtLoginProcessingFilter() throws Exception {
        JwtLoginProcessingFilter filter
            = new JwtLoginProcessingFilter(LOGIN_URL, objectMapper);
        filter.setAuthenticationManager(authenticationManagerBean);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return filter;
    }

}
