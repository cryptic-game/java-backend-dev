package net.cryptic_game.backend.admin;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(Config.class)
public class Bootstrap {

    public static void main(final String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @GetMapping("/user")
    public Principal user(@AuthenticationPrincipal final Principal principal) {
        return principal;
    }

    @GetMapping(value = "/auth", produces = MediaType.TEXT_HTML_VALUE)
    public String auth(@AuthenticationPrincipal final Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return "<script>window.close();</script>This window will be closed.";
        } else {
            return "Not authenticated!";
        }
    }

    @Bean
    GroupedOpenApi websiteApi() {
        return GroupedOpenApi.builder()
                .group("cryptic-website")
                .pathsToMatch("/website/**")
                .build();
    }

    @Bean
    GroupedOpenApi serverApi() {
        return GroupedOpenApi.builder()
                .group("cryptic-server")
                .pathsToMatch("/server_management/**")
                .build();
    }
}
