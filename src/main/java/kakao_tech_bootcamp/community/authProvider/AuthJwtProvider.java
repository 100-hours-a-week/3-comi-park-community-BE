package kakao_tech_bootcamp.community.authProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import kakao_tech_bootcamp.community.common.JwtProperties;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthJwtProvider implements AuthProvider {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties ;

    @Override
    public List<ResponseCookie> issue(Member member) {
        long accessTtlSec = 15 * 60;

        String credential = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();

        ResponseCookie cookie = ResponseCookie.from("sid", credential)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7) // 일주일
                .build();

        return List.of(cookie);
    }

    @Override
    public AuthInfo validate(String credential) {
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
    public List<ResponseCookie> invalidate(String credential) {
        ResponseCookie cookie = ResponseCookie.from("sid", credential)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return List.of(cookie);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
