package kakao_tech_bootcamp.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ImageResponseDto {
    private Integer id;
    private String url; // presigned PUT URL과 CloudFront GET URL 자리
    private String objectKey;
    private String filename;
}
