package kakao_tech_bootcamp.community.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.Getter;

@Getter
public class MemberAvailabilityDto implements BaseResponse {
    @Email(message = "이메일 형식이 유효하지 않습니다")
    @Size(max = 50, message = "이메일 형식이 유효하지 않습니다")
    private String email;

    @Pattern(regexp = "^\\S{1,10}$", message = "닉네임 형식이 유효하지 않습니다")
    private String nickname;
}
