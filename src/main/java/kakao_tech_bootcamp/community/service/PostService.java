package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.PostCreateRequestDto;
import kakao_tech_bootcamp.community.dto.PostResponseDto;
import kakao_tech_bootcamp.community.entity.*;
import kakao_tech_bootcamp.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostAdditionalRepository postAdditionalRepository;
    private final PostStatRepository postStatRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    public void savePost(Integer currentMemberId, PostCreateRequestDto dto) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        Image image = null;

        if (dto.getImage() != null) {
            image = imageRepository.findById(dto.getImage().getId())
                    .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다"));
        }

        savePost(new Post(dto.getTitle(), dto.getContent(), member, image));
    }

    @Transactional(readOnly = true)
    public PostResponseDto findPost(Integer currentMemberId, Integer postId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        PostStat postStat = postStatRepository.findById(postId).orElseGet(() -> findPostStat(postId));
        boolean isLiked = memberPostLikeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(postId, currentMemberId);

        postStat.incrementViewCount();
        postStatRepository.save(postStat);

        return PostResponseDto.of(post, isLiked, postStat);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findPosts(Integer currentMemberId, Integer lastPostId, Integer limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
        List<Post> posts = lastPostId == null
                ? postRepository.findByIsDeletedFalseOrderByIdDesc(pageable)
                : postRepository.findByIsDeletedFalseAndIdLessThanOrderByIdDesc(lastPostId, pageable);

        return posts.stream().map(x -> PostResponseDto.of(x,
                memberPostLikeRepository.existsByMemberPostLikeIdPostIdAndMemberPostLikeIdMemberId(x.getId(), currentMemberId),
                postStatRepository.findById(x.getId()).orElseGet(() -> findPostStat(x.getId())))).toList();
    }

    private void savePost(Post post) {
        Post savePost = postRepository.save(post);
        postAdditionalRepository.save(new PostAdditional(savePost));
        postStatRepository.save(new PostStat(savePost.getId()));
    }

    private PostStat findPostStat(Integer postId) {
        int viewCount = postAdditionalRepository.countByPostId(postId);
        int likeCount = memberPostLikeRepository.countByMemberPostLikeIdPostId(postId);
        int commentCount = commentRepository.countByPostId(postId);
        return new PostStat(postId, viewCount, likeCount, commentCount);
    }
}
