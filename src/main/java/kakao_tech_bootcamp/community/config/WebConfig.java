package kakao_tech_bootcamp.community.config;

import kakao_tech_bootcamp.community.common.AuthInterceptor;
import kakao_tech_bootcamp.community.common.CurrentMemberResolver;
import kakao_tech_bootcamp.community.common.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final CurrentMemberResolver currentMemberResolver;

    @Autowired
    public WebConfig(AuthInterceptor authInterceptor, CurrentMemberResolver currentMemberResolver) {
        this.authInterceptor = authInterceptor;
        this.currentMemberResolver = currentMemberResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(File.separator + "s3" + File.separator + "**")
                .addResourceLocations("file:" + StorageProperties.getLocalStoragePath());
    }
}
