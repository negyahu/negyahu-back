package ga.negyahu.music.config;

import ga.negyahu.music.security.annotation.LoginUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.Errors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile({"dev", "prod"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .ignoredParameterTypes(Errors.class, LoginUser.class)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("ga.negyahu.music"))
            .paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Fantimate API Document")
            .version("1.0")
            .description("Fantimate 서버 API 문서")
            .build();
    }

}
