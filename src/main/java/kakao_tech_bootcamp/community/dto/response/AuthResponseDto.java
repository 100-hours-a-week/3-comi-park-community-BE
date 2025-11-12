package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.AuthDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto implements BaseResponse {
    private AuthDto auth;

    public static AuthResponseDto of(AuthInfo auth) {
        return new AuthResponseDto(AuthDto.of(auth.getId()));
    }
}
