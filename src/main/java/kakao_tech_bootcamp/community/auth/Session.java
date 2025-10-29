package kakao_tech_bootcamp.community.auth;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Session {
    private String sessionId;
    private final Integer memberId;
    private final String memberEmail;
    private String userAgent; // TODO: request 헤더의 userAgent로 로그인 위치 기록
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private final LocalDateTime refreshExpiredAt;

    public Session(String sessionId, Integer memberId, String memberEmail) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusMinutes(15);
        refreshExpiredAt = createdAt.plusDays(30);
    }

    public Session(String sessionId, Integer memberId, String memberEmail, String userAgent) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.userAgent = userAgent;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusMinutes(15);
        refreshExpiredAt = createdAt.plusDays(30);
    }

    public void updateSession(String sessionId) {
        this.sessionId = sessionId;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusMinutes(15);
    }

    public boolean isSessionExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public boolean isRefreshExpired() {
        return LocalDateTime.now().isAfter(refreshExpiredAt);
    }
}
