package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.ConflictException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.entity.MemberPostLike;
import kakao_tech_bootcamp.community.entity.MemberPostLikeId;
import kakao_tech_bootcamp.community.entity.PostStat;
import kakao_tech_bootcamp.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberPostLikeService {
    private final MemberPostLikeRepository likeRepository;
    private final PostStatRepository postStatRepository;
    private final PostRepository postRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final CommentRepository commentRepository;

    public int saveLike(Integer currentMemberId, Integer postId) {
        if (!postRepository.existsByIdAndIsDeletedFalse(postId)) {
            throw new NotFoundException("게시글을 찾을 수 없습니다");
        }

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (likeExists) {
            throw new ConflictException("회원이 이미 좋아요한 게시글입니다");
        }

        likeRepository.save(new MemberPostLike(postId, currentMemberId));

        PostStat postStat = postStatRepository.findById(postId).orElseGet(() -> createPostStat(postId));
        postStat.incrementLikeCount();
        postStatRepository.save(postStat);

        return postStat.getLikeCount();
    }

    public int removeLike(Integer currentMemberId, Integer postId) {
        if (!postRepository.existsByIdAndIsDeletedFalse(postId)) {
            throw new NotFoundException("게시글을 찾을 수 없습니다");
        }

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (!likeExists) {
            throw new NotFoundException("좋아요를 찾을 수 없습니다");
        }

        likeRepository.deleteById(new MemberPostLikeId(postId, currentMemberId));

        PostStat postStat = postStatRepository.findById(postId).orElseGet(() -> createPostStat(postId));
        postStat.decrementLikeCount();
        postStatRepository.save(postStat);

        return postStat.getLikeCount();
    }

    // TODO: PostService와 중복 메서드. PostStatService 따로 빼내야 할 듯
    private PostStat createPostStat(Integer postId) {
        int likeCount = memberPostLikeRepository.countByMemberPostLikeIdPostId(postId);
        int commentCount = commentRepository.countByPostId(postId);
        return new PostStat(postId, likeCount, commentCount);
    }
}
