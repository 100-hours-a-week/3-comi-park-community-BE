package kakao_tech_bootcamp.community.dto.response.basic;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberDto implements BaseResponse {
    private Integer id;
    private String email;
    private String nickname;
    private ImageDto image;
    private LocalDateTime createdAt;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .image(ImageDto.of(member.getImage()))
                .createdAt(member.getCreatedAt())
                .build();
    }
}
