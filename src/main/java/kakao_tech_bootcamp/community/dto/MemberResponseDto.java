package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private Integer id;
    private String email;
    private String nickname;
    private ImageResponseDto image;
    private LocalDateTime createdAt;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail(),
                member.getNickname(),ImageResponseDto.of(member.getImage()), member.getCreatedAt());
    }
}
