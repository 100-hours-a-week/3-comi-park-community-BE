package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.ForbiddenException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.CommentRequestDto;
import kakao_tech_bootcamp.community.dto.CommentResponseDto;
import kakao_tech_bootcamp.community.entity.Comment;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.entity.Post;
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

    public CommentResponseDto saveComment(Integer currentMemberId, Integer postId, CommentRequestDto dto) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));
        Comment comment = commentRepository.save(new Comment(post, member, dto.getContent()));
        return CommentResponseDto.of(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findComments(Integer postId, Integer lastCommentId, Integer limit) {
        postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));

        Pageable pageable = PageRequest.of(0, limit);
        List<Comment> comments = lastCommentId == null
                ? commentRepository.findByPostIdOrderByIdDesc(postId, pageable)
                : commentRepository.findByPostIdAndIdLessThanOrderByIdDesc(postId, lastCommentId, pageable);

        return comments.stream().map(CommentResponseDto::of).toList();
    }

    public Map<String, Object> modifyComment(Integer currentMemberId, Integer postId, Integer commentId, CommentRequestDto dto) {
        postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다"));

        if (!Objects.equals(comment.getMember().getId(), currentMemberId)) {
            throw new ForbiddenException("댓글을 작성한 회원만 수정할 수 있습니다");
        }

        comment.changeContent(dto.getContent());

        return Map.of("content", comment.getContent());
    }

    public void removeComment(Integer currentMemberId, Integer postId, Integer commentId) {
        postRepository.findByIdAndIsDeletedFalse(postId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다"));

        if (!Objects.equals(comment.getMember().getId(), currentMemberId)) {
            throw new ForbiddenException("댓글을 작성한 회원만 삭제할 수 있습니다");
        }

        commentRepository.delete(comment);
    }
}
