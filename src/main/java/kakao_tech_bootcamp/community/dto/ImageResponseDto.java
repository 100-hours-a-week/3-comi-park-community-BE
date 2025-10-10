package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {
    private Integer id;
    private String url; // presigned PUT URL과 CloudFront GET URL 자리
    private String objectKey;
    private String filename;

    public static ImageResponseDto of(Image image) {
        return new ImageResponseDto(image.getId(), null, image.getObjectKey(), image.getFilename());
    }
}
