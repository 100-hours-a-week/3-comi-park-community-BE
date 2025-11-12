package kakao_tech_bootcamp.community.dto.response.basic;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthDto implements BaseResponse {
    private Integer id;

    public static AuthDto of(Integer id) {
        return new AuthDto(id);
    }
}
