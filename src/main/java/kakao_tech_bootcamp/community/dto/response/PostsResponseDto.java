package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostsResponseDto implements BaseResponse {
    private List<PostAllResponseDto> posts;

    public static PostsResponseDto of(List<PostAllResponseDto> posts) {
        return new PostsResponseDto(posts);
    }
}
