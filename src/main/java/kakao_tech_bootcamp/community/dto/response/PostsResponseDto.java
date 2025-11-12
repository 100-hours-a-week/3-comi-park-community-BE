package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostsResponseDto implements BaseResponse {
    private List<PostAllResponseDto> posts;
    private boolean hasNext;

    public static PostsResponseDto of(List<PostAllResponseDto> posts, boolean hasNext) {
        return new PostsResponseDto(posts, hasNext);
    }
}
