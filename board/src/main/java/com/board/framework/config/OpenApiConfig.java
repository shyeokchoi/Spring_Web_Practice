package com.board.framework.config;

import java.util.Arrays;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private String port;

    @Value("${springdoc.version}")
    String springdocVersion;

    private String TOKEN_KEY = "Access-Token";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(getComponents())
                .security(Arrays.asList(new SecurityRequirement().addList(TOKEN_KEY)));
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:" + port);

        return openApi -> openApi
                .info(getApiInfo())
                .addServersItem(localServer);
    }

    // 스웨거 UI 정보
    private Info getApiInfo() {
        return new Info()
                .title("web_study board API")
                .version(springdocVersion)
                .description("Spring Boot 연습용 게시판의 API 입니다.");
    }

    // Jwt 토큰 컴포넌트
    private Components getComponents() {
        return new Components().addSecuritySchemes(TOKEN_KEY, getJwtSecurityScheme());
    }

    private SecurityScheme getJwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(TOKEN_KEY)
                .description("Access Token");
    }
}