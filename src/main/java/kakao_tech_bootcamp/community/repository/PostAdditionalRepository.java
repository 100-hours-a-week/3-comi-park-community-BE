package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.PostAdditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAdditionalRepository extends JpaRepository<PostAdditional, Integer> {
    @Query("select pa.viewCount from PostAdditional pa where pa.id = :postId")
    Integer findViewCountByPostId(@Param("postId") Integer postId);
}
