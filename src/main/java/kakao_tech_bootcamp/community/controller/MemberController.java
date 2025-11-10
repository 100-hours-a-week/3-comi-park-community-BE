package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.CookieManager;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.common.jwt.JwtProperties;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.common.session.SessionProperties;
import kakao_tech_bootcamp.community.dto.*;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final SessionProperties sessionProperties;
    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final CookieManager cookieManager;

    @PostMapping("/availability/email")
    public ResponseEntity<?> isAvailableEmail(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByEmail(memberAvailabilityDto);
        return ResponseFactory.ok();
    }

    @PostMapping("/availability/nickname")
    public ResponseEntity<?> isAvailableNickname(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByNickname(memberAvailabilityDto);
        return ResponseFactory.ok();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, MemberResponseDto>>> saveMember(@RequestBody @Validated MemberCreateRequestDto memberCreateRequestDto) {
        MemberResponseDto member = memberService.saveMember(memberCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(Map.of("member", member)));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<?> findMember(@PathVariable Integer memberId) {
        MemberResponseDto member = memberService.findMember(memberId);
        return ResponseFactory.ok(member);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> modifyMember(@CurrentMember AuthInfo authInfo,
                                       @PathVariable Integer memberId,
                                       @RequestBody @Validated MemberUpdateRequestDto updateMemberRequestDto) {
        Map<String, Object> changes = memberService.modifyMember(authInfo.getId(), memberId, updateMemberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.modified(changes));
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
