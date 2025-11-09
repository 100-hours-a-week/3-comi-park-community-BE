package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.CommentExceptionCode;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberExceptionCode;
import kakao_tech_bootcamp.community.common.exceptions.code.PostExceptionCode;
import kakao_tech_bootcamp.community.dto.CommentRequestDto;
import kakao_tech_bootcamp.community.dto.CommentResponseDto;
import kakao_tech_bootcamp.community.entity.Comment;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.entity.PostStat;
import kakao_tech_bootcamp.community.repository.CommentRepository;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import kakao_tech_bootcamp.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostStatService postStatService;

    public Map<String, Object> saveComment(Integer currentMemberId, Integer postId, CommentRequestDto dto) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new CustomException(MemberExceptionCode.NOT_FOUND));
        Comment comment = commentRepository.save(new Comment(post, member, dto.getContent()));

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        int commentCount = postStatService.incrementCommentCount(postStat);

        return Map.of("comment", CommentResponseDto.of(comment), "commentCount", commentCount);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findComments(Integer postId, Integer lastCommentId, Integer limit) {
        postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));

        Pageable pageable = PageRequest.of(0, limit);
        List<Comment> comments = lastCommentId == null
                ? commentRepository.findByPostIdOrderByIdDesc(postId, pageable)
                : commentRepository.findByPostIdAndIdLessThanOrderByIdDesc(postId, lastCommentId, pageable);

        return comments.stream().map(CommentResponseDto::of).toList();
    }

    public Map<String, Object> modifyComment(Integer currentMemberId, Integer postId, Integer commentId, CommentRequestDto dto) {
        postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CommentExceptionCode.NOT_FOUND));

        if (!Objects.equals(comment.getMember().getId(), currentMemberId)) {
            throw new CustomException(CommentExceptionCode.FORBIDDEN_UPDATE);
        }

        comment.changeContent(dto.getContent());

        return Map.of("content", comment.getContent());
    }

    public int removeComment(Integer currentMemberId, Integer postId, Integer commentId) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new CustomException(PostExceptionCode.NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CommentExceptionCode.NOT_FOUND));

        if (!Objects.equals(comment.getMember().getId(), currentMemberId)) {
            throw new CustomException(CommentExceptionCode.FORBIDDEN_DELETE);
        }

        PostStat postStat = postStatService.findPostStat(post)
                .orElseGet(() -> postStatService.savePostStatInitializedByCount(post));
        int commentCount = postStatService.decrementCommentCount(postStat);

        commentRepository.delete(comment);

        return commentCount;
    }
}
