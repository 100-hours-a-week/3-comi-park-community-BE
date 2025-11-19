package kakao_tech_bootcamp.community.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class PublicEndpointProperties {
    private List<EndPoint> publicEndpoints;

    @Getter
    @Setter
    public static class EndPoint {
        private String path;
        private String method;
    }
}
