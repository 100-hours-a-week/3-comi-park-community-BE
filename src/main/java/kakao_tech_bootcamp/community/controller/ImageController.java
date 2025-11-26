package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageService;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ImageCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<CommonResponse<BaseResponse>> saveMemberImage(@CurrentMember AuthInfo authInfo,
                                                                        @RequestParam("category") ImageCategory category,
                                                                        @RequestParam(value = "file") MultipartFile file) {
        return ResponseFactory.created(imageService.saveImage(category, file));
    }
}
