package kakao_tech_bootcamp.community.dto.response.basic;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CountDto implements BaseResponse {
    private Integer count;

    public static CountDto of(Integer count) {
        return new CountDto(count);
    }
}
