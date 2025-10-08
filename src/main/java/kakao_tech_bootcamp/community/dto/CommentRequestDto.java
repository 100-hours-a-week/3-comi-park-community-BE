package kakao_tech_bootcamp.community.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank(message = "내용이 존재하지 않습니다")
    private String content;
}
