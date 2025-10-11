package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberPostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class MemberPostLikeController {
    private final MemberPostLikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Integer>>> saveLike(@CurrentMember AuthInfo authInfo, @PathVariable Integer postId) {
        int likeCount = likeService.saveLike(authInfo.getId(), postId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create_success", Map.of("likeCount", likeCount)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Map<String, Integer>>> removeLike(@CurrentMember AuthInfo authInfo, @PathVariable Integer postId) {
        int likeCount = likeService.removeLike(authInfo.getId(), postId);
        return ResponseEntity.ok(new ApiResponse<>("delete_success", Map.of("likeCount", likeCount)));
    }
}
