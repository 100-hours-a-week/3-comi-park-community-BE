package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.PostCreateRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> savePost(@CurrentMember AuthInfo authInfo,
                                                @RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.savePost(authInfo.getId(), postCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("post_create_success", null));
    }
}
