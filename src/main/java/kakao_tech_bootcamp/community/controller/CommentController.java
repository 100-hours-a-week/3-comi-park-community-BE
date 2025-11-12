package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.request.CommentRequestDto;
import kakao_tech_bootcamp.community.dto.response.ChangedResponseDto;
import kakao_tech_bootcamp.community.dto.response.CommentResponseDto;
import kakao_tech_bootcamp.community.dto.response.CommentsResponseDto;
import kakao_tech_bootcamp.community.dto.response.basic.CountDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<BaseResponse>> saveComment(
            @CurrentMember AuthInfo authInfo,
            @PathVariable Integer postId,
            @RequestBody @Validated CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.saveComment(authInfo.getId(), postId, commentRequestDto);
        return ResponseFactory.created(commentResponseDto);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<BaseResponse>> findComments(
            @PathVariable Integer postId,
            @RequestParam(value = "lastCommentId", required = false) Integer lastCommentId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        CommentsResponseDto commentsResponseDto = commentService.findComments(postId, lastCommentId, limit);
        return ResponseFactory.ok(commentsResponseDto);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse<BaseResponse>> modifyComment(@CurrentMember AuthInfo authInfo,
                                                     @PathVariable("postId") Integer postId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody @Validated CommentRequestDto commentRequestDto) {
        ChangedResponseDto changes = commentService.modifyComment(authInfo.getId(), postId, commentId, commentRequestDto);
        return ResponseFactory.ok(changes);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<BaseResponse>> removeComment(@CurrentMember AuthInfo authInfo,
                                                     @PathVariable("postId") Integer postId,
                                                     @PathVariable("commentId") Integer commentId) {
        CountDto countDto = commentService.removeComment(authInfo.getId(), postId, commentId);
        return ResponseFactory.ok(countDto);
    }
}
