package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.PostAllResponseDto;
import kakao_tech_bootcamp.community.dto.PostCreateRequestDto;
import kakao_tech_bootcamp.community.dto.PostResponseDto;
import kakao_tech_bootcamp.community.dto.PostUpdateRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, PostResponseDto>>> savePost(@CurrentMember AuthInfo authInfo,
                                                @RequestBody PostCreateRequestDto postCreateRequestDto) {
        PostResponseDto post = postService.savePost(authInfo.getId(), postCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(Map.of("post", post)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<PostAllResponseDto>>>> findPosts(@CurrentMember AuthInfo authInfo,
                                                                                        @RequestParam(value = "lastPostId", required = false) Integer lastPostId,
                                                                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        List<PostAllResponseDto> posts = postService.findPosts(authInfo.getId(), lastPostId, limit);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("posts", posts)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<Map<String, PostResponseDto>>> findPost(@CurrentMember AuthInfo authInfo, @PathVariable Integer postId) {
        PostResponseDto post = postService.findPost(authInfo.getId(), postId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("post", post)));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> modifyPost(@CurrentMember AuthInfo authInfo,
                                                  @PathVariable Integer postId,
                                                  @RequestBody @Validated PostUpdateRequestDto postUpdateRequestDto) {
        Map<String, Object> changes = postService.modifyPost(authInfo.getId(), postId, postUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.modified(changes));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> removePosts(@CurrentMember AuthInfo authInfo,
                                      @RequestParam(value = "before", required = false) LocalDate before,
                                      @RequestParam(value = "after", required = false) LocalDate after,
                                      @RequestParam(value = "writer", required = false) Integer memberId) {
        // TODO: 회원 권한 확인 필요
        long affectedRows = postService.removePosts(before, after, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.removed(Map.of("affectedRows", affectedRows)));
    }
}
