package kakao_tech_bootcamp.community.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberAvailabilityDto {
    @Email(message = "이메일 형식이 유효하지 않습니다")
    @Size(max = 50, message = "이메일 형식이 유효하지 않습니다")
    private String email;

    @Pattern(regexp = "^\\S{1,10}$", message = "닉네임 형식이 유효하지 않습니다")
    private String nickname;
}
