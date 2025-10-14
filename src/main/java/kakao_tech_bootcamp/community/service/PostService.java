package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.ForbiddenException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.PostAllResponseDto;
import kakao_tech_bootcamp.community.dto.PostCreateRequestDto;
import kakao_tech_bootcamp.community.dto.PostResponseDto;
import kakao_tech_bootcamp.community.dto.PostUpdateRequestDto;
import kakao_tech_bootcamp.community.entity.*;
import kakao_tech_bootcamp.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PostStatService postStatService;

    public PostResponseDto savePost(Integer currentMemberId, PostCreateRequestDto dto) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        Image image = dto.getImage() != null
                ? imageService.modifyImageStatusById(dto.getImage().getId(), ImageStatus.ACTIVE)
                : null;

        Post savePost = postRepository.save(new Post(dto.getTitle(), dto.getContent(), member, image)); // 바로 flush 해야 참조 무결성 유지
        postStatService.savePostStat(savePost); // 지연 쓰기로 인해 log 모두 출력 후 post_stat insert (flush) 실행

        return PostResponseDto.of(savePost);
    }

    /**
     * 게시글 정보와 게시글 통계 정보(조회수, 좋아요 수, 댓글 수), 조회자의 좋아요 여부를 반환합니다.
     * 단, 데이터베이스에 통계 정보가 없으면 새로운 트랜잭션을 열어서 post_stat 테이블에 통계 정보를 저장한 후에 반환합니다.
     * @param currentMemberId
     * @param postId
     * @return
     */
    @Transactional(readOnly = true)
    public PostResponseDto findPost(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        boolean isLiked = memberPostLikeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        postStatService.incrementViewCount(postStat);

        return PostResponseDto.of(post, isLiked, postStat);
    }

    @Transactional(readOnly = true)
    public List<PostAllResponseDto> findPosts(Integer currentMemberId, Integer lastPostId, Integer limit) {
        return postRepository.findAllIdLessThanCustom(currentMemberId, lastPostId, limit);
    }

    public Map<String, Object> modifyPost(Integer currentMemberId, Integer postId, PostUpdateRequestDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        if (!Objects.equals(post.getMember().getId(), currentMemberId)) {
            throw new ForbiddenException("게시글을 작성한 회원만 수정할 수 있습니다");
        }

        Map<String, Object> changes = new HashMap<>();

        if (dto.getPostDeleted()) {
            post.markDeleted(); // 관련 이미지 상태는 여전히 ACTIVE (배치 프로그램에 의해 삭제되지 않도록)
            changes.put("postDeleted", true);
            return changes; // soft delete 요청 시 그 외 게시글 수정 무시
        }

        if (dto.getTitle() != null) {
            post.changeTitle(dto.getTitle());
            changes.put("title", post.getTitle());
        }

        if (dto.getContent() != null) {
            post.changeContent(dto.getContent());
            changes.put("content", post.getContent());
        }

        /*
         이미지 삭제 true이고, 새 이미지도 전송하지 않았다면 기존 이미지 삭제 처리
         만약 이미지 삭제 true더라도 새 이미지를 전송했다면 새로운 이미지로의 변경으로 간주함
         */
        if (dto.getImageDeleted() && dto.getImage() == null && post.getImage() != null) {
            Image previousImage = post.getImage();
            post.changeImage(null);
            imageService.removeImage(previousImage.getId(), previousImage.getObjectKey());

            changes.put("image", post.getImage());
        }

        if (dto.getImage() != null) {
            Image previousImage = post.getImage();

            Image currentImage = imageService.modifyImageStatusById(dto.getImage().getId(), ImageStatus.ACTIVE);
            post.changeImage(currentImage);

            if (previousImage != null) {
                imageService.removeImage(previousImage.getId(), previousImage.getObjectKey());
            }

            changes.put("image", post.getImage());
        }

        return changes;
    }

    public long removePosts(LocalDate before, LocalDate after, Integer memberId) {
        if (before == null && after == null && memberId == null) {
            return 0; // 쿼리스트링 없으면 아무 작업 X
        }

        // LocalDateTime createdAt과 비교하기 위해 레포지터리가 아닌 서비스 계층에서 미리 변환
        LocalDateTime convertedBefore = before != null ? before.plusDays(1).atStartOfDay() : null;
        LocalDateTime convertedAfter = after != null ? after.atStartOfDay() : null;

        return postRepository.deleteAllByIsDeletedTrueAndDynamicFilters(memberId, convertedBefore, convertedAfter);
    }
}
