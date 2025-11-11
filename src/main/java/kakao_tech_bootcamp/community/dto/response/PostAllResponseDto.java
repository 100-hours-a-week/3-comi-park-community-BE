package kakao_tech_bootcamp.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.PostDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostAllResponseDto implements BaseResponse {
    private PostDto post;
    private boolean isLiked;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    @QueryProjection
    public PostAllResponseDto(
            Integer id,
            String title,
            LocalDateTime createdAt,
            MemberReferenceDto member,
            boolean isLiked,
            Integer viewCount,
            Integer likeCount,
            Integer commentCount
    ) {
        post = PostDto.builder().id(id).title(title).createdAt(createdAt).member(member).build();
        this.isLiked = isLiked;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
