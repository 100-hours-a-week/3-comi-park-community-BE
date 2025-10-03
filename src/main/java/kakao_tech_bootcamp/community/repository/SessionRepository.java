package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.service.Session;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void save(String sessionId, Session session) {
        sessions.putIfAbsent(sessionId, session);
    }

    // JpaRepository와의 통일성 위해 리턴 값 Optional로 감쌈
    public Optional<Session> findById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    // JpaRepository와의 통일성 위해 리턴 값 Optional로 감쌈
    public Optional<Session> delete(String sessionId) {
        return Optional.ofNullable(sessions.remove(sessionId));
    }
}
