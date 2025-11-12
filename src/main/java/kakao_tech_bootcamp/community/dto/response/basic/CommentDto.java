package kakao_tech_bootcamp.community.dto.response.basic;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto implements BaseResponse {
    private Integer id;
    private MemberDto member;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .member(MemberDto.of(comment.getMember()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
