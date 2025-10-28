package kakao_tech_bootcamp.community.authProvider;

import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthSessionProvider implements AuthProvider {
    private final static int SESSION_LIMIT = 5;

    private final SessionRepository sessionRepository;

    @Override
    public List<ResponseCookie> issue(Member member) {
        List<AuthInfo> memberSessions = sessionRepository.findAllByMemberId(member.getId());

        /*
         각 회원은 로그인 세션 최대 5개까지 유지 가능
         이미 5개의 세션이 등록돼 있는데 새로 로그인 시도 시 가장 오래된 세션 정보 자동 삭제
         */
        if (memberSessions.size() >= SESSION_LIMIT) {
            memberSessions.stream()
                    .min(Comparator.comparing(AuthInfo::getCreatedAt))
                    .ifPresent(sessionRepository::delete);
        }

        String sessionId = UUID.randomUUID().toString();
        AuthInfo session = new AuthInfo(member.getId());

        sessionRepository.save(sessionId, session);

        ResponseCookie cookie = ResponseCookie.from("sid", sessionId)
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
        AuthInfo session = sessionRepository.findById(credential)
                .orElseThrow(() -> new NotFoundException("인증 정보를 찾을 수 없습니다"));

        if (session.isExpired()) {
            sessionRepository.deleteById(credential);
            throw new UnauthorizedException("인증 정보가 만료됐습니다");
        }

        return session;
    }

    @Override
    public List<ResponseCookie> invalidate(String credential) {
        // 로그아웃하는 상황에 만약 sessionRepository에 삭제할 세션ID가 없더라도 404 에러를 낼 이유가 없음
        sessionRepository.deleteById(credential);

        ResponseCookie cookie = ResponseCookie.from("sid", credential)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return List.of(cookie);
    }
}
