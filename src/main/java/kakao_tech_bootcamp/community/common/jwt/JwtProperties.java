package kakao_tech_bootcamp.community.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private String issuer;

    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AccessToken {
        private String key;
        private String path;
        private long maxAge;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class RefreshToken {
        private String key;
        private String path;
        private long maxAge;
    }
}
