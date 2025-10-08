package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Integer id;
    private MemberResponseDto member;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(comment.getId(), MemberResponseDto.of(comment.getMember()),
                comment.getContent(), comment.getCreatedAt());
    }
}
