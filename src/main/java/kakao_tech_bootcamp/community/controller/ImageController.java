package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService localImageService) {
        this.imageService = localImageService;
    }

    @PostMapping("/members")
    public ResponseEntity<ApiResponse> saveMemberImage(@RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("members", file);
        Map<String, ImageResponseDto> data = Map.of("image", imageResponseDto);
        return ResponseEntity.ok(new ApiResponse<>("upload_success", data));
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse> savePostImage(@CurrentMember AuthInfo authInfo, @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("posts", file);
        Map<String, ImageResponseDto> data = Map.of("image", imageResponseDto);
        return ResponseEntity.ok(new ApiResponse<>("upload_success", data));
    }
}
