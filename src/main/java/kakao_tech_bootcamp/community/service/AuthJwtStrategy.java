package kakao_tech_bootcamp.community.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kakao_tech_bootcamp.community.common.JwtProperties;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
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

//@Service
@RequiredArgsConstructor
public class AuthJwtStrategy implements AuthStrategy {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties ;

    @Override
    public String issue(AuthRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        /*
         회원을 인증할 수 없다는 401이 논리적으로 옳을 수 있지만
         보안을 고려하여 이메일/비밀번호 중 무엇이 틀렸는지 힌트를 주지 않기 위해 404로 통일
         */
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        long accessTtlSec = 15 * 60;

        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(jwtProperties.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public AuthInfo validate(String credential) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecretKey()).build()
                .parseClaimsJws(credential).getBody();

        return new AuthInfo(
                (Integer) body.get("id"),
                toLocalDateTime(body.getIssuedAt()),
                toLocalDateTime(body.getExpiration())
        );
    }

    @Override
    public void invalidate(String credential) {

    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
