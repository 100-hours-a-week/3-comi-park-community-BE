package kakao_tech_bootcamp.community.authProvider;

import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

//@Component
@RequiredArgsConstructor
public class AuthSessionProvider implements AuthProvider {
    private final static int SESSION_LIMIT = 5;

    private final SessionRepository sessionRepository;

    @Override
    public List<Credential> issue(Member member) {
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

        Credential credential = issueCredential(member);
        Credential refreshCredential = issueRefreshCredential(member);

        Session session = new Session(member.getId(), refreshCredential.getValue());
        sessionRepository.save(credential.getValue(), session);

        return List.of(credential, refreshCredential);
    }

    @Override
    public Credential issueCredential(Member member) {
        long sessionIdTtlSeconds = 60 * 60 * 24 * 7; // 일주일
        String sessionId = UUID.randomUUID().toString();

        return new Credential("credential", sessionId, sessionIdTtlSeconds, "/");

    }

    @Override
    public Credential issueRefreshCredential(Member member) {
        long refreshIdTtlSeconds = 60 * 60 * 24 * 30; // 일주일
        String refreshId = UUID.randomUUID().toString();

        return new Credential("refreshCredential", refreshId, refreshIdTtlSeconds, "/auth");
    }

    @Override
    public AuthInfo validate(String credential, String refreshCredential) {
        Session session = sessionRepository.findById(credential)
                .orElseThrow(() -> new NotFoundException("인증 정보를 찾을 수 없습니다"));

        if (session.isSessionExpired()) {
            sessionRepository.deleteById(credential);
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        }

        return new AuthInfo(session.getId(), session.getCreatedAt(), session.getExpiredAt());
    }

    @Override
    public List<Credential> invalidate(String credential, String refreshCredential) {
        // 로그아웃하는 상황에 만약 sessionRepository에 삭제할 세션ID가 없더라도 404 에러를 낼 이유가 없음
        sessionRepository.deleteById(credential);
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

}
