package kakao_tech_bootcamp.community.dto.response;

import kakao_tech_bootcamp.community.common.response.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ChangedResponseDto implements BaseResponse {
    private final Map<String, Object> changes = new HashMap<>();

    public void add(String key, Object data) {
        changes.put(key, data);
    }
}
