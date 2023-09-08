package com.board.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Value("${server.port}")
  private String port;

  @Value("${springdoc.version}")
  String springdocVersion;

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("web_study board API")
        .version(springdocVersion)
        .description("Spring Boot 연습용 게시판의 API 입니다.");

    Server localServer = new Server();
    localServer.setDescription("local");
    localServer.setUrl("http://localhost:" + port);

    return new OpenAPI()
        .components(new Components()).addServersItem(localServer)
        .info(info);
  }
}