package kakao_tech_bootcamp.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    @NotBlank(message = "제목 형식이 유효하지 않습니다")
    @Size(max = 26, message = "제목 형식이 유효하지 않습니다")
    private String title;

    @NotBlank(message = "내용이 존재하지 않습니다")
    @Size(min = 1, message = "내용이 존재하지 않습니다")
    private String content;

    private ImageRequestDto image;
}
