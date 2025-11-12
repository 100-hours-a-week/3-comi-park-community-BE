package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberLikeExceptionCode;
import kakao_tech_bootcamp.community.common.exceptions.code.PostExceptionCode;
import kakao_tech_bootcamp.community.dto.response.basic.CountDto;
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

    public CountDto saveLike(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (likeExists) {
            throw new CustomException(MemberLikeExceptionCode.DUPLICATED_LIKE);
        }

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        int likeCount = postStatService.incrementLikeCount(postStat);

        likeRepository.save(new MemberPostLike(postId, currentMemberId));
        return CountDto.of(likeCount);
    }

    public CountDto removeLike(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));

        boolean likeExists = likeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        if (!likeExists) {
            throw new CustomException(MemberLikeExceptionCode.NOT_FOUND);
        }

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        int likeCount = postStatService.decrementLikeCount(postStat);

        likeRepository.deleteById(new MemberPostLikeId(postId, currentMemberId));

        return CountDto.of(likeCount);
    }
}
