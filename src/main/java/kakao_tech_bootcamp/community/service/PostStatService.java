package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.entity.PostStat;
import kakao_tech_bootcamp.community.repository.CommentRepository;
import kakao_tech_bootcamp.community.repository.MemberPostLikeRepository;
import kakao_tech_bootcamp.community.repository.PostStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostStatService {
    private final PostStatRepository postStatRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final CommentRepository commentRepository;

    public PostStat findPostStat(Post post) {
        return postStatRepository.findById(post.getId()).orElseGet(() -> savePostStatInitializedByCount(post));
    }

    public void savePostStat(Post post) {
        postStatRepository.save(new PostStat(post));
    }

    private PostStat savePostStatInitializedByCount(Post post) {
        // FIXME: @Transactional(readOnly = true) 메서드 안에서 호출 시 flush 안 됨
        int likeCount = memberPostLikeRepository.countByMemberPostLikeIdPostId(post.getId());
        int commentCount = commentRepository.countByPostId(post.getId());
        log.info("likeCount = {}, commentCount = {}", likeCount, commentCount);
        return postStatRepository.save(new PostStat(post, likeCount, commentCount));
    }
}
