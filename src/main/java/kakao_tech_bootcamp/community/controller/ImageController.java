package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.authProvider.AuthInfo;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(Map.of("image", imageResponseDto)));
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Map<String, ImageResponseDto>>> savePostImage(@CurrentMember AuthInfo authInfo,
                                                                                    @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("posts", file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(Map.of("image", imageResponseDto)));
    }
}
