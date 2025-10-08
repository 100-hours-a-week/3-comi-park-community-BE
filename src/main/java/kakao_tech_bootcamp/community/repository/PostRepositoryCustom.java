package kakao_tech_bootcamp.community.repository;

import java.time.LocalDateTime;

public interface PostRepositoryCustom {
    long deleteAllByIsDeletedTrueAndDynamicFilters(Integer memberId, LocalDateTime before, LocalDateTime after);
}
