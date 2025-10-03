package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);

    public Optional<Member> findByEmail(String email);
}
