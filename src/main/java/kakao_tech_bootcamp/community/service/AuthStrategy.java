package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.dto.AuthRequestDto;

public interface AuthStrategy {
    String issue(AuthRequestDto dto);

    String validate(String credential);

    void invalidate(String credential);
}
