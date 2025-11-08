package kakao_tech_bootcamp.community.common;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@Getter
public class JwtProperties {
    private final Key secretKey;
    private final String issuer;

    public JwtProperties(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.issuer}") String issuer) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        this.issuer = issuer;
    }
}
