package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);
}
