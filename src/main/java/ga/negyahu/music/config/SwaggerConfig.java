package ga.negyahu.music.config;

import com.fasterxml.classmate.TypeResolver;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.dto.ManagerDto;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.security.annotation.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;


@Profile({"dev", "prod"})
@Configuration
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
            .additionalModels(
                typeResolver.resolve(AgencyDto.class),
                typeResolver.resolve(ResultMessage.class),
                typeResolver.resolve(AccountDto.class),
                typeResolver.resolve(AccountCreateDto.class),
                typeResolver.resolve(AgencySearch.class),
                typeResolver.resolve(ManagerDto.class)
            )
            .alternateTypeRules(
                AlternateTypeRules
                    .newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(Page.class))
            )
            .apiInfo(apiInfo())
            .ignoredParameterTypes(Errors.class, LoginUser.class)
            .useDefaultResponseMessages(false)
            .securitySchemes(Arrays.asList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("ga.negyahu.music"))
            .paths(PathSelectors.any()).build()
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(LocalDateTime.class, String.class)
            ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Fantimate API Document")
            .version("1.0")
            .description("Fantimate ?????? API ??????")
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("token", "Authorization", "header");
    }

    @Schema(description = "????????? ????????? ?????? ?????????")
    static class Page {

        @Schema(description = "????????? ??????(0..N)",defaultValue = "1")
        private Integer p;

        @Schema(description = "????????? ??????", allowableValues = "range[0, 100]",defaultValue = "10")
        private Integer s;

        @Schema(description = "???????????? ex): ???????????? ,ASC | DESC", defaultValue = "[]")
        private List<String> sort;
    }


}
