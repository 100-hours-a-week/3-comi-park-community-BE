package kakao_tech_bootcamp.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.entity.Image;
import lombok.Getter;

@Getter
public class ImageReferenceDto {
    private Integer id;
    private String objectKey;

    @QueryProjection
    public ImageReferenceDto(Integer id, String objectKey) {
        this.id = id;
        this.objectKey = objectKey;
    }

    public static ImageReferenceDto of(Image image) {
        return image != null ? new ImageReferenceDto(image.getId(), image.getObjectKey()) : null;
    }
}
