package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    int countByPostId(Integer postId);

    @EntityGraph(attributePaths = {"member", "member.image"})
    List<Comment> findByPostIdOrderByIdDesc(Integer postId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "member.image"})
    List<Comment> findByPostIdAndIdLessThanOrderByIdDesc(Integer postId, Integer lastCommentId, Pageable pageable);
}
