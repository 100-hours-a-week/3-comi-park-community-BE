package kakao_tech_bootcamp.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.common.StorageProperties;
import kakao_tech_bootcamp.community.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private Integer id;
    private String url;
    private String objectKey;
    private String filename;

    @QueryProjection
    public ImageResponseDto(Integer id, String objectKey, String filename) {
        this.id = id;
        this.objectKey = objectKey;
        this.filename = filename;
        this.url = StorageProperties.getStaticBaseUrl() + objectKey;
    }

    public static ImageResponseDto of(Image image) {
        return new ImageResponseDto(image.getId(), image.getObjectKey(), image.getFilename());
    }
}
