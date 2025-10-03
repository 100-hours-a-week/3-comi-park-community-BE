package kakao_tech_bootcamp.community.service;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Session {
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public Session(String email) {
        this.email = email;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusDays(7);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
