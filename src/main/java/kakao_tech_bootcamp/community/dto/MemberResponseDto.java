package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class MemberResponseDto {
    private Integer id;
    private String email;
    private String nickname;
    private Image image;
    private LocalDateTime createdAt;
}
