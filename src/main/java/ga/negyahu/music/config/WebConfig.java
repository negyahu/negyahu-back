package ga.negyahu.music.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
        final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 20);

        SortHandlerMethodArgumentResolver sortArgumentResolver = new SortHandlerMethodArgumentResolver();

        PageableHandlerMethodArgumentResolver pageableArgumentResolver =
            new PageableHandlerMethodArgumentResolver(sortArgumentResolver);

        pageableArgumentResolver.setOneIndexedParameters(true); // 페이지인덱스를 클라이언트가 1부터 인식하도록
        pageableArgumentResolver.setPageParameterName("p");
        pageableArgumentResolver.setSizeParameterName("s");
        pageableArgumentResolver.setMaxPageSize(100);
        pageableArgumentResolver.setFallbackPageable(DEFAULT_PAGEABLE);
        resolvers.add(pageableArgumentResolver);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(2000000000);
        return multipartResolver;
    }

}
