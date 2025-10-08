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
}
