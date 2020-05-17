package com.sunny.minigame.score.backend.datastore.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("score-update-api")
                .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

    @Bean
    public Docket getsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("position-fetch-api")
                .apiInfo(apiInfo()).select().paths(getPaths()).build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/score"));
    }

    private Predicate<String> getPaths() {
        return or(regex("/.*/position"),regex("/highscorelist"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Minigame API")
                .description("Minigame Backend API reference for developers")
                .termsOfServiceUrl("https://en.wikipedia.org/wiki/GNU_General_Public_License")
                .contact("dummy_id_84@dummy.com").license("GNU_General_Public_License")
                .licenseUrl("dummy_id_84@dummy.com").version("1.0").build();
    }

}