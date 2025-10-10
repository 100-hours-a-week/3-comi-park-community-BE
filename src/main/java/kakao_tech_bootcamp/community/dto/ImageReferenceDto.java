package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageReferenceDto {
    private Integer id;
    private String objectKey;

    public static ImageReferenceDto of(Image image) {
        return image != null ? new ImageReferenceDto(image.getId(), image.getObjectKey()) : null;
    }

    public static ImageReferenceDto of(Integer id, String objectKey) {
        return id != null && objectKey != null ? new ImageReferenceDto(id, objectKey) : null;
    }
}
