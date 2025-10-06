package kakao_tech_bootcamp.community.repository;

import kakao_tech_bootcamp.community.entity.PostStat;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostStatRepository {
    /*
     게시글 관련 통계 데이터(조회수, 좋아요수, 댓글수)를 관리하는 Redis 등의 캐시용 인메모리 DB
     */
    private Map<Integer, PostStat> store = new ConcurrentHashMap<>();

    public void save(PostStat postStat) {
        store.put(postStat.getPostId(), postStat);
    }

    public Optional<PostStat> findById(Integer postId) {
        return Optional.ofNullable(store.get(postId));
    }

    public void incrementViewCount(Integer postId) {
        Optional.ofNullable(store.get(postId)).ifPresent(PostStat::incrementViewCount);
    }

    public void incrementLikeCount(Integer postId) {
        Optional.ofNullable(store.get(postId)).ifPresent(PostStat::incrementLikeCount);
    }

    public void decrementLikeCount(Integer postId) {
        Optional.ofNullable(store.get(postId)).ifPresent(PostStat::decrementLikeCount);
    }

    public void incrementCommentCount(Integer postId) {
        Optional.ofNullable(store.get(postId)).ifPresent(PostStat::incrementCommentCount);
    }

    public void decrementCommentCount(Integer postId) {
        Optional.ofNullable(store.get(postId)).ifPresent(PostStat::decrementCommentCount);
    }

    public void updateLikeCount(Integer postId, int likeCount) {
        Optional.ofNullable(store.get(postId)).ifPresent(x -> x.changeLikeCount(likeCount));
    }

    public void updateCommentCount(Integer postId, int commentCount) {
        Optional.ofNullable(store.get(postId)).ifPresent(x -> x.changeCommentCount(commentCount));
    }

    public void deleteById(Integer postId) {
        store.remove(postId);
    }
}
