package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberReferenceDto {
    private String nickname;
    private ImageReferenceDto image;

    public static MemberReferenceDto of(Member member) {
        return new MemberReferenceDto(member.getNickname(), ImageReferenceDto.of(member.getImage()));
    }
}
