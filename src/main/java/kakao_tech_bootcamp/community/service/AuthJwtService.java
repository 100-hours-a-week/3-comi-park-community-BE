package kakao_tech_bootcamp.community.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.common.jwt.JwtProvider;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthJwtService {
    private final JwtProvider jwtProvider;
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

        return Map.of("accessToken", jwtProvider.issueAccessToken(member.getId(), member.getEmail()),
                "refreshToken", jwtProvider.issueRefreshToken(member.getId(), member.getEmail()));
    }

    public AuthInfo validate(String accessToken) {
        if (accessToken == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다");
        }

        try {
            Claims body = jwtProvider.parse(accessToken).getBody();
            return new AuthInfo((Integer) body.get("id"));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        } catch (SignatureException e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다");
        }
    }

    public String reIssue(String refreshToken) {
        try {
            Claims body = jwtProvider.parse(refreshToken).getBody();
            return jwtProvider.issueAccessToken((Integer) body.get("id"), (String) body.get("email"));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        } catch (SignatureException e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다");
        }
    }
}
