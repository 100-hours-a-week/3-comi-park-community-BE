package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.authProvider.AuthInfo;
import kakao_tech_bootcamp.community.authProvider.AuthProvider;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthProvider authProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ResponseCookie> issue(AuthRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        /*
         회원을 인증할 수 없다는 401이 논리적으로 옳을 수 있지만
         보안을 고려하여 이메일/비밀번호 중 무엇이 틀렸는지 힌트를 주지 않기 위해 404로 통일
         */
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        return authProvider.issue(member);
    }

    public AuthInfo validate(String credential) {
        return authProvider.validate(credential);
    }

    public List<ResponseCookie> invalidate(String credential) {
        return authProvider.invalidate(credential);
    }
}
