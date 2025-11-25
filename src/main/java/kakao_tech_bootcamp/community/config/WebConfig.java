package kakao_tech_bootcamp.community.config;

import kakao_tech_bootcamp.community.common.AuthInterceptor;
import kakao_tech_bootcamp.community.common.CurrentMemberResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final CurrentMemberResolver currentMemberResolver;

    @Value("${storage.local-path}")
    private String localPath;

    @Autowired
    public WebConfig(AuthInterceptor authInterceptor, CurrentMemberResolver currentMemberResolver) {
        this.authInterceptor = authInterceptor;
        this.currentMemberResolver = currentMemberResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/static/**",
                        "/s3/members/*",
                        "/auth/**",
                        "/terms", "/privacy" // thymeleaf
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(File.separator + "s3" + File.separator + "**")
                .addResourceLocations("file:" + localPath);
    }
}
