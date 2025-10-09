package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.PostStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatRepository extends JpaRepository<PostStat, Integer> {
}
