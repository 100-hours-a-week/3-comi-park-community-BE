package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.authProvider.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public void deleteById(String sessionId) {
        sessions.remove(sessionId);
    }

    public void delete(Session session) {
        sessions.entrySet().stream()
                .filter(entry -> Objects.equals(session, entry.getValue()))
                .findFirst()
                .ifPresent(entry -> sessions.remove(entry.getKey()));
    }

    public List<Session> findAllByMemberId(Integer memberId) {
        return sessions.values().stream()
                .filter(session -> Objects.equals(session.getId(), memberId))
                .toList();
    }
}
