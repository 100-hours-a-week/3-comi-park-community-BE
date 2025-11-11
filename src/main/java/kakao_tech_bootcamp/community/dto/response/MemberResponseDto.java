package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.MemberDto;
import kakao_tech_bootcamp.community.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDto implements BaseResponse {
    private MemberDto member;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(MemberDto.of(member));
    }
}
