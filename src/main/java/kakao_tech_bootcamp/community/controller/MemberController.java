package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.*;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Void> removeMember(@CurrentMember AuthInfo authInfo, @PathVariable Integer memberId) {
        memberService.removeMember(authInfo.getId(), memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
