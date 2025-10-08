package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
    Optional<Post> findByIdAndIsDeletedFalse(Integer postId);

    boolean existsByIdAndIsDeletedFalse(Integer postId);

    // TODO: 카디널리티 폭발... 해결 필요
    @EntityGraph(attributePaths = {"member", "member.image", "image"})
    List<Post> findByIsDeletedFalseOrderByIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"member", "member.image", "image"})
    List<Post> findByIsDeletedFalseAndIdLessThanOrderByIdDesc(Integer lastPostId, Pageable pageable);
}
