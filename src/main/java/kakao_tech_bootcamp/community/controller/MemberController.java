package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.*;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Log4j2
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/availability/email")
    public ResponseEntity<ApiResponse<Void>> isAvailableEmail(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByEmail(memberAvailabilityDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success());
    }

    @PostMapping("/availability/nickname")
    public ResponseEntity<ApiResponse<Void>> isAvailableNickname(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByNickname(memberAvailabilityDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, MemberResponseDto>>> saveMember(@RequestBody @Validated MemberCreateRequestDto memberCreateRequestDto) {
        MemberResponseDto member = memberService.saveMember(memberCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(Map.of("member", member)));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Map<String, MemberResponseDto>>> findMember(@PathVariable Integer memberId) {
        MemberResponseDto member = memberService.findMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("member", member)));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> modifyMember(@CurrentMember AuthInfo authInfo,
                                       @PathVariable Integer memberId,
                                       @RequestBody @Validated MemberUpdateRequestDto updateMemberRequestDto) {
        Map<String, Object> changes = memberService.modifyMember(authInfo.getId(), memberId, updateMemberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.modified(changes));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(@CookieValue("credential") String credential,
                                                          @CurrentMember AuthInfo authInfo,
                                                          @PathVariable Integer memberId) {
        memberService.removeMember(authInfo.getId(), memberId);

        ResponseCookie cookie = ResponseCookie.from("credential", credential)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(0) // 일주일
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, cookie.toString())
                .body(ApiResponse.success());
    }
}
