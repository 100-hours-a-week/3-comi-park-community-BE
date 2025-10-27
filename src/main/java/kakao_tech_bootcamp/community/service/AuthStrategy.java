package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import org.springframework.http.ResponseCookie;

import java.util.List;

public interface AuthStrategy {
    List<ResponseCookie> issue(AuthRequestDto dto);

    AuthInfo validate(String credential);

    List<ResponseCookie> invalidate(String credential);
}
