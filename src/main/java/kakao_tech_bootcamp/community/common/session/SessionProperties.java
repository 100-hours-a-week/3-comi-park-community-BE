package kakao_tech_bootcamp.community.common.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "session")
public class SessionProperties {
    private SessionId sessionId;
    private RefreshId refreshId;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class SessionId {
        private String key;
        private String path;
        private long maxAge;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class RefreshId {
        private String key;
        private String path;
        private long maxAge;
    }
}
