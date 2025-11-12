package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.response.basic.CountDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberPostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class MemberPostLikeController {
    private final MemberPostLikeService likeService;

    @PostMapping
    public ResponseEntity<CommonResponse<BaseResponse>> saveLike(@CurrentMember AuthInfo authInfo, @PathVariable Integer postId) {
        CountDto countDto = likeService.saveLike(authInfo.getId(), postId);
        return ResponseFactory.created(countDto);

    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<BaseResponse>> removeLike(@CurrentMember AuthInfo authInfo, @PathVariable Integer postId) {
        CountDto countDto = likeService.removeLike(authInfo.getId(), postId);
        return ResponseFactory.ok(countDto);
    }
}
