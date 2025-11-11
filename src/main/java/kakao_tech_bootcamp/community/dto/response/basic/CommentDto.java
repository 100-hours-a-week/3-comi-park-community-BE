package kakao_tech_bootcamp.community.dto.response.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto implements BaseResponse {
    private Integer id;
    private MemberDto member;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto of(Comment comment) {
        return new CommentDto(comment.getId(), MemberDto.of(comment.getMember()),
                comment.getContent(), comment.getCreatedAt());
    }
}
