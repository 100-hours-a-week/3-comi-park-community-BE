package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.controller.ImageUploadRequest;
import kakao_tech_bootcamp.community.dto.response.ImageResponseDto;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ImageCategory;

public interface ImageStorageStrategy {
    ImageResponseDto saveImage(ImageCategory category, ImageUploadRequest uploadRequest);
    void removeImage(Integer imageId, String objectKey);
    ImageStorage getImageStorage();
}
