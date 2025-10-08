package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.dto.*;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/availability/email")
    public ResponseEntity<ApiResponse<Void>> isAvailableEmail(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByEmail(memberAvailabilityDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("ok", null));
    }

    @PostMapping("/availability/nickname")
    public ResponseEntity<ApiResponse<Void>> isAvailableNickname(@RequestBody @Validated MemberAvailabilityDto memberAvailabilityDto) {
        memberService.existsByNickname(memberAvailabilityDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("ok", null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveMember(@RequestBody @Validated MemberCreateRequestDto memberCreateRequestDto) {
        memberService.saveMember(memberCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("register_success", null));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Map<String, MemberResponseDto>>> findMember(@PathVariable Integer memberId) {
        MemberResponseDto member = memberService.findMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("ok", Map.of("member", member)));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> modifyMember(@CurrentMember AuthInfo authInfo,
                                       @PathVariable Integer memberId,
                                       @RequestBody @Validated MemberUpdateRequestDto updateMemberRequestDto) {
        memberService.modifyMember(authInfo.getId(), memberId, updateMemberRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@CurrentMember AuthInfo authInfo, @PathVariable Integer memberId) {
        memberService.removeMember(authInfo.getId(), memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
