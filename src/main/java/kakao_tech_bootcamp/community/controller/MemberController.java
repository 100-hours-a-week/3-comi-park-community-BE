package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.CookieManager;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.jwt.JwtProperties;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.common.session.SessionProperties;
import kakao_tech_bootcamp.community.dto.request.MemberAvailabilityDto;
import kakao_tech_bootcamp.community.dto.request.MemberCreateRequestDto;
import kakao_tech_bootcamp.community.dto.request.MemberUpdateRequestDto;
import kakao_tech_bootcamp.community.dto.response.ChangedResponseDto;
import kakao_tech_bootcamp.community.dto.response.MemberResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final SessionProperties sessionProperties;
    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final CookieManager cookieManager;

    @PostMapping("/availability/email")
    public ResponseEntity<CommonResponse<Void>> isAvailableEmail(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByEmail(memberAvailabilityDto);
        return ResponseFactory.ok();
    }

    @PostMapping("/availability/nickname")
    public ResponseEntity<CommonResponse<Void>> isAvailableNickname(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByNickname(memberAvailabilityDto);
        return ResponseFactory.ok();
    }

    @PostMapping
    public ResponseEntity<CommonResponse<BaseResponse>> saveMember(@RequestBody @Validated MemberCreateRequestDto memberCreateRequestDto) {
        MemberResponseDto memberResponseDto = memberService.saveMember(memberCreateRequestDto);
        return ResponseFactory.created(memberResponseDto);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<BaseResponse>> findMember(@PathVariable Integer memberId) {
        MemberResponseDto memberResponseDto = memberService.findMember(memberId);
        return ResponseFactory.ok(memberResponseDto);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<CommonResponse<BaseResponse>> modifyMember(@CurrentMember AuthInfo authInfo,
                                       @PathVariable Integer memberId,
                                       @RequestBody @Validated MemberUpdateRequestDto updateMemberRequestDto) {
        ChangedResponseDto changes = memberService.modifyMember(authInfo.getId(), memberId, updateMemberRequestDto);
        return ResponseFactory.ok(changes);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponse<Void>> removeMember(@CookieValue(value = "sid", required = false) String sessionId,
                                                          @CookieValue(value = "accessToken", required = false) String accessToken,
                                                          @CurrentMember AuthInfo authInfo,
                                                          @PathVariable Integer memberId) {
        memberService.removeMember(authInfo.getId(), memberId);

        ResponseCookie sidCookie =  cookieManager.destroyCookie(sessionProperties.getSessionId().getKey(), sessionProperties.getSessionId().getPath());
        ResponseCookie accessTokenCookie = cookieManager.destroyCookie(jwtProperties.getAccessToken().getKey(), jwtProperties.getAccessToken().getPath());

        return ResponseFactory.ok(List.of(sidCookie, accessTokenCookie));
    }
}
