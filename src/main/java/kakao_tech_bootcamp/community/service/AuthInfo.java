package kakao_tech_bootcamp.community.service;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthInfo {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public AuthInfo(Integer id) {
        this.id = id;
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusDays(7);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
