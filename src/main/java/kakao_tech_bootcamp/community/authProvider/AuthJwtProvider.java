package kakao_tech_bootcamp.community.authProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import kakao_tech_bootcamp.community.common.JwtProperties;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthJwtProvider implements AuthProvider {
    private final JwtProperties jwtProperties ;

    @Override
    public List<Credential> issue(Member member) {
        return List.of(issueCredential(member), issueRefreshCredential(member));
    }

    @Override
    public Credential issueCredential(Member member) {
        long accessTtlSeconds = 15 * 60;  // 15분
        String accessToken = createJwtToken(member, accessTtlSeconds);
        return new Credential("credential", accessToken, accessTtlSeconds, "/");
    }

    @Override
    public Credential issueRefreshCredential(Member member) {
        long refreshTtlSeconds = 60 * 60 * 24 * 30; // 30일
        String refreshToken = createJwtToken(member, refreshTtlSeconds);
        return new Credential("refreshCredential", refreshToken, refreshTtlSeconds, "/auth");
    }


    @Override
    public AuthInfo validate(String credential, String refreshCredential) {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey()).build()
                    .parseClaimsJws(credential).getBody();

            return new AuthInfo(
                    (Integer) body.get("id"),
                    toLocalDateTime(body.getIssuedAt()),
                    toLocalDateTime(body.getExpiration())
            );
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        } catch (SignatureException e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다");
        }
    }

    @Override
    public List<Credential> invalidate(String credential, String refreshCredential) {
        return List.of(invalidateCredential(credential), invalidateRefreshCredential(refreshCredential));
    }

    @Override
    public Credential invalidateCredential(String credential) {
        return new Credential("credential", credential, 0, "/");
    }

    @Override
    public Credential invalidateRefreshCredential(String refreshCredential) {
        return new Credential("refreshCredential", refreshCredential, 0, "/auth");
    }

    private String createJwtToken(Member member, long ttlSeconds) {
        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(ttlSeconds)))
                .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
