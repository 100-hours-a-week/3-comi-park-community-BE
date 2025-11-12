package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.response.ImageResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/members")
    public ResponseEntity<CommonResponse<BaseResponse>> saveMemberImage(@RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("members", file);
        return ResponseFactory.created(imageResponseDto);
    }

    @PostMapping("/posts")
    public ResponseEntity<CommonResponse<BaseResponse>> savePostImage(@CurrentMember AuthInfo authInfo,
                                                                                    @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponseDto imageResponseDto = imageService.saveImage("posts", file);
        return ResponseFactory.ok(imageResponseDto);
    }
}
