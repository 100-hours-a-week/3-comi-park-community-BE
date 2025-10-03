package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.service.AuthInfo;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {
    private final Map<String, AuthInfo> sessions = new ConcurrentHashMap<>();

    public void save(String sessionId, AuthInfo session) {
        sessions.putIfAbsent(sessionId, session);
    }

    // JpaRepository와의 통일성 위해 리턴 값 Optional로 감쌈
    public Optional<AuthInfo> findById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public void delete(String sessionId) {
        sessions.remove(sessionId);
    }
}
