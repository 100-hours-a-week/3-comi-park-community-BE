package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.dto.PostAllResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    List<PostAllResponseDto> findAllIdLessThanCustom(Integer memberId, Integer lastPostId, Integer limit);
    long deleteAllByIsDeletedTrueAndDynamicFilters(Integer memberId, LocalDateTime before, LocalDateTime after);
}
