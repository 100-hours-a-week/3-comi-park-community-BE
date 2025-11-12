package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.ImageDto;
import kakao_tech_bootcamp.community.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageResponseDto implements BaseResponse {
    private ImageDto image;

    public static ImageResponseDto of(Image image) {
        return new ImageResponseDto(ImageDto.of(image));
    }
}
