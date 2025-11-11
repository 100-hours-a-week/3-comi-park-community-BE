package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.request.PostCreateRequestDto;
import kakao_tech_bootcamp.community.dto.response.PostsResponseDto;
import kakao_tech_bootcamp.community.dto.response.basic.CountDto;
import kakao_tech_bootcamp.community.dto.response.basic.PostDto;
import kakao_tech_bootcamp.community.dto.request.PostUpdateRequestDto;
import kakao_tech_bootcamp.community.dto.response.PostResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse<BaseResponse>> savePost(@CurrentMember AuthInfo authInfo,
                                                                 @RequestBody PostCreateRequestDto postCreateRequestDto) {
        PostResponseDto postResponseDto = postService.savePost(authInfo.getId(), postCreateRequestDto);
        return ResponseFactory.created(postResponseDto);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<BaseResponse>> findPosts(
            @CurrentMember AuthInfo authInfo,
            @RequestParam(value = "lastPostId", required = false) Integer lastPostId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        PostsResponseDto postsResponseDto = postService.findPosts(authInfo.getId(), lastPostId, limit);
        return ResponseFactory.ok(postsResponseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<BaseResponse>> findPost(
            @CurrentMember AuthInfo authInfo,
            @PathVariable Integer postId,
            @RequestParam(value = "editMode", defaultValue = "false") boolean editMode) {
        PostResponseDto postResponseDto = postService.findPost(authInfo.getId(), postId, editMode);
        return ResponseFactory.ok(postResponseDto);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse<BaseResponse>> modifyPost(
            @CurrentMember AuthInfo authInfo,
            @PathVariable Integer postId,
            @RequestBody @Validated PostUpdateRequestDto postUpdateRequestDto) {
        PostDto changes = postService.modifyPost(authInfo.getId(), postId, postUpdateRequestDto);
        return ResponseFactory.ok(changes);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<BaseResponse>> removePosts(
            @CurrentMember AuthInfo authInfo,
            @RequestParam(value = "before", required = false) LocalDate before,
            @RequestParam(value = "after", required = false) LocalDate after,
            @RequestParam(value = "writer", required = false) Integer memberId) {
        // TODO: 회원 권한 확인 필요
        CountDto countDto = postService.removePosts(before, after, memberId);
        return ResponseFactory.ok(countDto);
    }
}
