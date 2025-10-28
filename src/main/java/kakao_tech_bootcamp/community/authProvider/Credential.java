package kakao_tech_bootcamp.community.authProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Credential {
    private String name;
    private String value;
    private long TtlSeconds;
    private String path;
}
