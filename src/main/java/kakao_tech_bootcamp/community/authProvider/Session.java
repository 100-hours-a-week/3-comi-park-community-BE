package kakao_tech_bootcamp.community.authProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Session {
    private Integer id;
    private String refreshId;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime refreshExpiredAt;

    public Session(Integer id, String refreshId) {
        this.id = id;
        this.refreshId = refreshId;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusDays(7);
        refreshExpiredAt = createdAt.plusDays(30);
    }

    public boolean isSessionExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public boolean isRefreshable() {
        return LocalDateTime.now().isBefore(refreshExpiredAt);
    }
}
