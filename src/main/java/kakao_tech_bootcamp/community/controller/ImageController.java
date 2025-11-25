package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.response.ImageResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageStorage;
import kakao_tech_bootcamp.community.service.ImageStorageStrategyFactory;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ImageCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageStorageStrategyFactory imageStorageStrategyFactory;

    @PostMapping()
    public ResponseEntity<CommonResponse<BaseResponse>> saveMemberImage(@CurrentMember AuthInfo authInfo,
                                                                        @RequestParam(value = "storage", required = false, defaultValue = "LOCAL") ImageStorage storage,
                                                                        @RequestParam("category") ImageCategory category,
                                                                        @RequestParam(value = "file", required = false) MultipartFile file) {
        ImageUploadRequest uploadRequest = ImageUploadRequest.builder()
                .file(file)
                .build();

        ImageResponseDto imageResponseDto = imageStorageStrategyFactory
                .getStrategy(storage)
                .saveImage(category, uploadRequest);

        return ResponseFactory.created(imageResponseDto);
    }
}
