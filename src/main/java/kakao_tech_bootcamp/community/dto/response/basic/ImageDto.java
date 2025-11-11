package kakao_tech_bootcamp.community.dto.response.basic;

import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.common.StorageProperties;
import kakao_tech_bootcamp.community.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageDto {
    private Integer id;
    private String url;
    private String objectKey;
    private String filename;

    @QueryProjection
    public ImageDto(Integer id, String objectKey, String filename) {
        this.id = id;
        this.objectKey = objectKey;
        this.filename = filename;
        this.url = StorageProperties.getStaticBaseUrl() + objectKey;
    }

    public static ImageDto of(Image image) {
        return image != null ? new ImageDto(image.getId(), image.getObjectKey(), image.getFilename()) : null;
    }
}
