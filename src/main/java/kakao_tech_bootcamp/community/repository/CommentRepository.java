package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    int countByPostId(Integer postId);
}
