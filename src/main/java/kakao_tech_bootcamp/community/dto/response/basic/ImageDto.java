package kakao_tech_bootcamp.community.dto.response.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDto {
    private Integer id;
    private String objectKey;
    private String filename;
    private String url;

    @QueryProjection
    public ImageDto(Integer id, String objectKey, String filename, String url) {
        this.id = id;
        this.objectKey = objectKey;
        this.filename = filename;
        this.url = url;
    }

    public static ImageDto of(Image image) {
        return image != null
                ? new ImageDto(image.getId(), image.getObjectKey(), image.getFilename(), image.getUrl())
                : null;
    }
}
