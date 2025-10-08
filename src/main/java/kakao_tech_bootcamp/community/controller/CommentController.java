package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.CommentRequestDto;
import kakao_tech_bootcamp.community.dto.CommentResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveComment(@CurrentMember AuthInfo authInfo,
                                                   @PathVariable Integer postId,
                                                   @RequestBody @Validated CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = commentService.saveComment(authInfo.getId(), postId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("comment_create_success", Map.of("comment", comment)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> findComments(@PathVariable Integer postId,
                                                   @RequestParam(value = "lastCommentId", required = false) Integer lastCommentId,
                                                   @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        List<CommentResponseDto> comments = commentService.findComments(postId, lastCommentId, limit);
        return ResponseEntity.ok(new ApiResponse<>("ok", Map.of("comments", comments)));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse> modifyComment(@CurrentMember AuthInfo authInfo,
                                                     @PathVariable("postId") Integer postId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody @Validated CommentRequestDto commentRequestDto) {
        CommentResponseDto comment = commentService.modifyComment(authInfo.getId(), postId, commentId, commentRequestDto);
        return ResponseEntity.ok(new ApiResponse<>("ok", Map.of("data", comment)));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> removeComment(@CurrentMember AuthInfo authInfo,
                                                     @PathVariable("postId") Integer postId,
                                                     @PathVariable("commentId") Integer commentId) {
        commentService.removeComment(authInfo.getId(), postId, commentId);
        return ResponseEntity.ok(new ApiResponse<>("ok", null));
    }
}
