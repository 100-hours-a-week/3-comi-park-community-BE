package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.auth.Session;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.SessionRepository;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthSessionService {
    private final static int SESSION_LIMIT = 5;

    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

        List<Session> memberSessions = sessionRepository.findAllByMemberId(member.getId());

        /*
         각 회원은 로그인 세션 최대 5개까지 유지 가능
         이미 5개의 세션이 등록돼 있는데 새로 로그인 시도 시 가장 오래된 세션 정보 자동 삭제
         */
        if (memberSessions.size() >= SESSION_LIMIT) {
            memberSessions.stream()
                    .min(Comparator.comparing(Session::getCreatedAt))
                    .ifPresent(sessionRepository::delete);
        }

        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, member.getId(),member.getEmail());

        sessionRepository.save(sessionId, session);

        return sessionId;
    }

    public AuthInfo validate(String credential) {
        Session session = sessionRepository.findById(credential)
                .orElseThrow(() -> new NotFoundException("인증 정보를 찾을 수 없습니다"));

        if (session.isSessionExpired()) {
            sessionRepository.deleteById(credential);
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        }

        return new AuthInfo(session.getMemberId());
    }

    public void invalidate(String credential) {
        // 로그아웃하는 상황에 만약 sessionRepository에 삭제할 세션ID가 없더라도 404 에러를 낼 이유가 없음
        sessionRepository.deleteById(credential);
    }
}
