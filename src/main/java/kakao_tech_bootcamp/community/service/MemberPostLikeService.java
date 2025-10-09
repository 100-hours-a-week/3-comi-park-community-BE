package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.ConflictException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.entity.MemberPostLike;
import kakao_tech_bootcamp.community.entity.MemberPostLikeId;
import kakao_tech_bootcamp.community.entity.Post;
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
    private final PostRepository postRepository;
    private final PostStatService postStatService;

    public int saveLike(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (likeExists) {
            throw new ConflictException("회원이 이미 좋아요한 게시글입니다");
        }

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        postStat.incrementLikeCount();

        likeRepository.save(new MemberPostLike(postId, currentMemberId));

        return postStat.getLikeCount();
    }

    public int removeLike(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (!likeExists) {
            throw new NotFoundException("좋아요를 찾을 수 없습니다");
        }

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        postStat.decrementLikeCount();

        likeRepository.deleteById(new MemberPostLikeId(postId, currentMemberId));

        return postStat.getLikeCount();
    }
}
