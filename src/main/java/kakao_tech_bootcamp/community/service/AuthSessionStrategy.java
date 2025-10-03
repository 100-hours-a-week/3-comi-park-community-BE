package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.SessionRepository;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthSessionStrategy implements AuthStrategy {
    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthSessionStrategy(SessionRepository sessionRepository, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.sessionRepository = sessionRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(dto.getEmail());

        sessionRepository.save(sessionId, session);

        return sessionId;
    }

    @Override
    public String validate(String credential) {
        Session session = sessionRepository.findById(credential)
                .orElseThrow(() -> new NotFoundException("인증 정보를 찾을 수 없습니다"));

        if (session.isExpired()) {
            sessionRepository.delete(credential);
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        }

        return session.getEmail();
    }

    @Override
    public void invalidate(String credential) {
        // 로그아웃하는 상황에 만약 sessionRepository에 삭제할 세션ID가 없더라도 404 에러를 낼 이유가 없음
        sessionRepository.delete(credential);
    }
}
