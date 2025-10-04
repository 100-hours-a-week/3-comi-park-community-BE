package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Log
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService localImageService) {
        this.imageService = localImageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> upload(@CurrentMember AuthInfo authInfo,
                                              @RequestParam("type") String type,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage(authInfo.getId(), type, file);
        Map<String, ImageResponseDto> data = Map.of("image", imageResponseDto);
        return ResponseEntity.ok(new ApiResponse<>("upload_success", data));
    }
}
