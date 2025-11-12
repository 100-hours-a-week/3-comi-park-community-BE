package kakao_tech_bootcamp.community.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    @Size(max = 26, message = "제목 형식이 유효하지 않습니다")
    private String title;

    @Size(min = 1, message = "내용이 존재하지 않습니다")
    private String content;

    private ImageRequestDto image;

    @NotNull(message = "imageDeleted 필드는 필수 값입니다")
    private Boolean imageDeleted;

    @NotNull(message = "postDeleted 필드는 필수 값입니다")
    private Boolean postDeleted;
}
