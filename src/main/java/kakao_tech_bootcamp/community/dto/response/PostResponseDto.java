package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.PostDto;
import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.entity.PostStat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PostResponseDto implements BaseResponse {
    private final PostDto post;
    private boolean isLiked;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    public static PostResponseDto of(Post post, boolean isLiked, PostStat postStat) {
        return new PostResponseDto(
                PostDto.of(post), isLiked,
                postStat.getViewCount(), postStat.getLikeCount(), postStat.getCommentCount()
        );
    }

    public static PostResponseDto of(Post post) {
        return new PostResponseDto(PostDto.of(post));
    }
}
