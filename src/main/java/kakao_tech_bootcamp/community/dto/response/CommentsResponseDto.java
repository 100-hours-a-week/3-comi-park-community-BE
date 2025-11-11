package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.CommentDto;
import kakao_tech_bootcamp.community.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentsResponseDto implements BaseResponse {
    private List<CommentDto> comments;
    private boolean hasNext;

    public static CommentsResponseDto of(List<Comment> comments, boolean hasNext) {
        return new CommentsResponseDto(comments.stream().map(CommentDto::of).toList(), hasNext);
    }
}
