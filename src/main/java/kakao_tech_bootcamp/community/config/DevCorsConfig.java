package kakao_tech_bootcamp.community.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("dev")
public class DevCorsConfig implements WebMvcConfigurer {
    @Value("${cors.allow-origins}")
    private String[] allowOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowOrigins)
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowCredentials(true);
    }
}
