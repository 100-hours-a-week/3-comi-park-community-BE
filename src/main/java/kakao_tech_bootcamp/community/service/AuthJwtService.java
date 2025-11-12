package kakao_tech_bootcamp.community.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.AuthExceptionCode;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberExceptionCode;
import kakao_tech_bootcamp.community.common.jwt.JwtProvider;
import kakao_tech_bootcamp.community.dto.request.AuthRequestDto;
import kakao_tech_bootcamp.community.dto.response.AuthResponseDto;
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
                .orElseThrow(() -> new CustomException(MemberExceptionCode.NOT_FOUND));

    /*
     회원을 인증할 수 없다는 401이 논리적으로 옳을 수 있지만
     보안을 고려하여 이메일/비밀번호 중 무엇이 틀렸는지 힌트를 주지 않기 위해 404로 통일
     */
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new CustomException(MemberExceptionCode.NOT_FOUND);
        }

        return Map.of("accessToken", jwtProvider.issueAccessToken(member.getId(), member.getEmail()),
                "refreshToken", jwtProvider.issueRefreshToken(member.getId(), member.getEmail()));
    }

    public AuthResponseDto validate(String accessToken) {
        if (accessToken == null) {
            throw new CustomException(AuthExceptionCode.MISSING_AUTH);
        }

        try {
            Claims body = jwtProvider.parse(accessToken).getBody();
            return AuthResponseDto.of(new AuthInfo((Integer) body.get("id")));
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthExceptionCode.EXPIRED_AUTH);
        } catch (SignatureException e) {
            throw new CustomException(AuthExceptionCode.INVALID_AUTH);
        }
    }

    public String reIssue(String refreshToken) {
        try {
            Claims body = jwtProvider.parse(refreshToken).getBody();
            return jwtProvider.issueAccessToken((Integer) body.get("id"), (String) body.get("email"));
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthExceptionCode.EXPIRED_AUTH);
        } catch (SignatureException e) {
            throw new CustomException(AuthExceptionCode.INVALID_AUTH);
        }
    }
}
