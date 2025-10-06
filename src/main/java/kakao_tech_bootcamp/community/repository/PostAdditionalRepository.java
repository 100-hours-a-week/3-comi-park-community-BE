package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.PostAdditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAdditionalRepository extends JpaRepository<PostAdditional, Integer> {
}
