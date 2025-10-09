package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.PostStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatRepository extends JpaRepository<PostStat, Integer> {
    /*
     @Modifying이라면 1차 캐시를 지워 영속성 컨텍스트와 DB 상태 불일치 제거 위해 clearAutomatically = true 필요하지만
     비동기 메서드 (asyncIncrementViewCount)에서만 실행되므로 해당 옵션 필요 X (영속성 컨텍스트는 트랜잭션마다 존재)
     */
    @Modifying
    @Query("update PostStat ps set ps.viewCount = ps.viewCount + 1 where ps.postId = :postId")
    void incrementViewCountById(@Param("postId") Integer postId);

    @Modifying
    @Query("update PostStat ps set ps.likeCount = ps.likeCount + 1 where ps.postId = :postId")
    void incrementLikeCountById(@Param("postId") Integer postId);

    @Modifying
    @Query("update PostStat ps set ps.likeCount = ps.likeCount - 1 where ps.postId = :postId")
    void decrementLikeCountById(@Param("postId") Integer postId);
}
