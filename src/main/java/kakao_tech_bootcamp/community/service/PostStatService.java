package kakao_tech_bootcamp.community.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.entity.PostStat;
import kakao_tech_bootcamp.community.repository.CommentRepository;
import kakao_tech_bootcamp.community.repository.MemberPostLikeRepository;
import kakao_tech_bootcamp.community.repository.PostStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostStatService {
    private final PostStatRepository postStatRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final CommentRepository commentRepository;
    private final PostStatAsyncService postStatAsyncService;

    @PersistenceContext
    private EntityManager em;

    public Optional<PostStat> findPostStat(Post post) {
        /*
         원래는 Optional 반환하지 않도록
         postStatRepository.findById(post.getId()).orElseGet(savePostStatInitializedByCount(post))
         하려 했지만, savePostStatInitializedByCount()에서 새로운 트랜잭션을 열려면 외부에서 호출돼야 해서
         외부에서 Optional 처리 후 필요하면 savePostStatInitializedByCount() 호출하도록 함
         */
        return postStatRepository.findById(post.getId());
    }

    public void savePostStat(Post post) {
        postStatRepository.save(new PostStat(post));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PostStat savePostStatInitializedByCount(Post post) {
        int likeCount = memberPostLikeRepository.countByMemberPostLikeIdPostId(post.getId());
        int commentCount = commentRepository.countByPostId(post.getId());
        /*
         매개변수 post는 다른 트랜잭션에서 가져와서 detached 상태이므로
         persist 하려고 하면 InvalidDataAccessApiUsageException: detached entity passed to persist 발생
         따라서 getReference() 통해 다시 post를 조회하지 않고 프록시 엔티티를 사용하도록 함
         */
        Post postRef = em.getReference(Post.class, post.getId());
        return postStatRepository.save(new PostStat(postRef, likeCount, commentCount));
    }

    public void incrementViewCount(PostStat postStat) {
        postStat.incrementViewCount(); // 응답에 사용하기 위해 메모리에서만 증가 처리
        postStatAsyncService.asyncIncrementViewCount(postStat.getPostId()); // 실제 DB 반영은 비동기로 처리
    }
}
