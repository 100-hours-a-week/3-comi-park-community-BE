package kakao_tech_bootcamp.community.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kakao_tech_bootcamp.community.dto.response.basic.CommentDto;
import kakao_tech_bootcamp.community.dto.response.basic.CountDto;
import kakao_tech_bootcamp.community.entity.Comment;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto extends CountDto {
    private final CommentDto comment;

    public CommentResponseDto(CommentDto comment) {
        super();
        this.comment = comment;
    }

    public CommentResponseDto(Integer count, CommentDto comment) {
        super(count);
        this.comment = comment;
    }

    public static CommentResponseDto of(Integer commentCount, Comment comment) {
        return new CommentResponseDto(commentCount, CommentDto.of(comment));
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(CommentDto.of(comment));
    }
}
