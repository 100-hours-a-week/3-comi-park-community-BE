package kakao_tech_bootcamp.community.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[~.!@#$%^&*()_\\-+=\\[\\]{}|\\\\;:'\",?/]).{8,20}$", message = "비밀번호 형식이 유효하지 않습니다")
    private String password;

    private String confirmedPassword;

    @Pattern(regexp = "^\\S{1,10}$", message = "닉네임 형식이 유효하지 않습니다")
    private String nickname;

    private ImageRequestDto image;

    @NotNull(message = "imageDeleted 필드는 필수 값입니다")
    private Boolean imageDeleted;
}
