package com.example.ipl.iplstats.config;

import com.example.ipl.iplstats.adapter.WebhookRequestAdapter;
import com.example.ipl.iplstats.adapter.WebhookResponseAdapter;
import com.google.cloud.dialogflow.v2.WebhookRequest;
import com.google.cloud.dialogflow.v2.WebhookResponse;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.example.ipl.iplstats.controller"))
                .paths(regex("/iplstats.*"))
                .build()
                .apiInfo(getApiInfo())
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));

    }

    private ApiInfo getApiInfo() {

        return new ApiInfo(
                "IPL Statistics API",
                "Provides the API to access IPL Data",
                "1.0",
                "TERMS OF SERVICE URL",
                new Contact("Uday", "URL", "udaykiranss@ymail.com"),
                "LICENSE",
                "LICENSE URL"
        );
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization",
                authorizationScopes));
    }

    @Bean
    public Gson gson() {
        GsonBuilder b = new GsonBuilder();
//        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
//        b.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
//        b.registerTypeAdapterFactory(TimestampTypeAdapter.FACTORY);
//        b.registerTypeAdapterFactory(LocalDateTypeAdapter.FACTORY);
//        b.registerTypeAdapterFactory(LocalDateTimeTypeAdapter.FACTORY);
        b.registerTypeAdapter(WebhookRequest.class, new WebhookRequestAdapter()).create();
        b.registerTypeAdapter(WebhookResponse.class, new WebhookResponseAdapter()).create();
        return b.create();
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(gson());
        gsonHttpMessageConverter.setSupportedMediaTypes(Arrays
                .asList(MediaType.APPLICATION_JSON));
        converters.add(gsonHttpMessageConverter);
    }

}
