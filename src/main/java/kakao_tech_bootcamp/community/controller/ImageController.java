package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/members")
    public ResponseEntity<ApiResponse<Map<String, ImageResponseDto>>> saveMemberImage(@RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("members", file);
        Map<String, ImageResponseDto> data = Map.of("image", imageResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("upload_success", data));
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Map<String, ImageResponseDto>>> savePostImage(@CurrentMember AuthInfo authInfo,
                                                                                    @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("posts", file);
        Map<String, ImageResponseDto> data = Map.of("image", imageResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("upload_success", data));
    }
}
