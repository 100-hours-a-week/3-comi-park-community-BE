package kakao_tech_bootcamp.community.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import kakao_tech_bootcamp.community.common.JwtProperties;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthJwtService {
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> issue(AuthRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

    /*
     회원을 인증할 수 없다는 401이 논리적으로 옳을 수 있지만
     보안을 고려하여 이메일/비밀번호 중 무엇이 틀렸는지 힌트를 주지 않기 위해 404로 통일
     */
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        return Map.of("accessToken", issueCredential(member.getId(), member.getEmail()),
                "refreshToken", issueRefreshCredential(member.getId(), member.getEmail()));
    }

    public String issueCredential(Integer memberId, String email) {
        long accessTtlSeconds = 15 * 60;  // 15분
        return createJwtToken(memberId, email, accessTtlSeconds);
    }

    private String issueRefreshCredential(Integer memberId, String email) {
        long refreshTtlSeconds = 60 * 60 * 24 * 30; // 30일
        return createJwtToken(memberId, email, refreshTtlSeconds);
    }

    public AuthInfo validate(String credential) {
        if (credential == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다");
        }

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

    public String reIssue(String refreshToken) {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey()).build()
                    .parseClaimsJws(refreshToken).getBody();

            return issueCredential((Integer) body.get("id"), (String) body.get("email"));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        } catch (SignatureException e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다");
        }
    }

    private String createJwtToken(Integer memberId, String email, long ttlSeconds) {
        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .claim("id", memberId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(ttlSeconds)))
                .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
