package com.wemade.board.framework.config;

import java.util.Arrays;

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
        .title("player console api")
        .version(springdocVersion)
        .description("");
    
      Server localServer = new Server();
      localServer.setDescription("local");
      localServer.setUrl("http://localhost:" + port);
    
      Server alphaServer = new Server();
      alphaServer.setDescription("alpha");
      alphaServer.setUrl("https://alpha-pconsole-api.wemix.co");
      

    return new OpenAPI()
        .components(new Components()).servers(Arrays.asList(localServer, alphaServer))
        .info(info);
  }
}