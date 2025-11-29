package kakao_tech_bootcamp.community.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageRequestDto {
    private Integer id;
    private String objectKey;
    private String filename;
    private String url;
}
