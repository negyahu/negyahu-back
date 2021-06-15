package ga.negyahu.music.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SessionLocaleResolver localResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*");
    }

    // Pageable
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        SortHandlerMethodArgumentResolver sortArgumentResolver = new SortHandlerMethodArgumentResolver();

        PageableHandlerMethodArgumentResolver pageableArgumentResolver =
            new PageableHandlerMethodArgumentResolver(
                sortArgumentResolver);

        pageableArgumentResolver.setOneIndexedParameters(true);
        pageableArgumentResolver.setPageParameterName("p");
        pageableArgumentResolver.setSizeParameterName("s");
        pageableArgumentResolver.setMaxPageSize(100);
        resolvers.add(pageableArgumentResolver);
    }

}
