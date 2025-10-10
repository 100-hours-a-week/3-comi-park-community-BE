package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
    Optional<Post> findByIdAndIsDeletedFalse(Integer postId);

    @Query(value = """
            SELECT
            	p.id, p.title, p.created_at,
            	m.nickname AS member_nickname, i.id AS member_image_id, i.object_key AS member_image_object_key,
            	EXISTS (SELECT 1 FROM member_post_like WHERE post_id = p.id AND member_id = :memberId) AS is_liked,
            	ps.view_count, ps.like_count, ps.comment_count
            FROM post p
            	JOIN member m ON m.id = p.member_id
            	JOIN post_stat ps ON ps.post_id = p.id
            	LEFT JOIN image i ON i.id = m.image_id
            WHERE p.is_deleted = false AND (:lastPostId IS NULL OR p.id < :lastPostId)
            ORDER BY p.id DESC
            LIMIT :limit;
            """, nativeQuery = true)
    List<Object[]> findAllIdLessThanCustom(@Param("memberId") Integer memberId,
                                           @Param("lastPostId") Integer lastPostId,
                                           @Param("limit") Integer limit);
}
