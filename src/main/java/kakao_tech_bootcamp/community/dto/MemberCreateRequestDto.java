package kakao_tech_bootcamp.community.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberCreateRequestDto {
    @Email(message = "이메일 형식이 유효하지 않습니다")
    @Size(max = 50, message = "이메일 형식이 유효하지 않습니다")
    private String email;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[~.!@#$%^&*()_\\-+=\\[\\]{}|\\\\;:'\",?/]).{8,20}$", message = "비밀번호 형식이 유효하지 않습니다")
    private String password;

    @NotBlank(message = "비밀번호가 일치하지 않습니다")
    private String confirmedPassword;

    @Pattern(regexp = "^\\S{1,10}$", message = "닉네임 형식이 유효하지 않습니다")
    private String nickname;

    private ImageReferenceDto image;
}
