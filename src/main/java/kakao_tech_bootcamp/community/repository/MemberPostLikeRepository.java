package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.MemberPostLike;
import kakao_tech_bootcamp.community.entity.MemberPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPostLikeRepository extends JpaRepository<MemberPostLike, MemberPostLikeId> {
    boolean existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(Integer postId, Integer memberId);
}
