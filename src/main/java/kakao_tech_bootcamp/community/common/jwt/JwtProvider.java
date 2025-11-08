package kakao_tech_bootcamp.community.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecretKey()));
    }

    public String issueAccessToken(Integer memberId, String email) {
        return createJwtToken(memberId, email, jwtProperties.getAccessToken().getMaxAge());
    }

    public String issueRefreshToken(Integer memberId, String email) {
        return createJwtToken(memberId, email, jwtProperties.getRefreshToken().getMaxAge());
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }

    private String createJwtToken(Integer memberId, String email, long ttlSeconds) {
        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .claim("id", memberId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(ttlSeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
