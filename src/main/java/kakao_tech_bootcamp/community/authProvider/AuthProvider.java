package kakao_tech_bootcamp.community.authProvider;

import kakao_tech_bootcamp.community.entity.Member;
import org.springframework.http.ResponseCookie;

import java.util.List;

public interface AuthProvider {
    List<ResponseCookie> issue(Member member);

    AuthInfo validate(String credential);

    List<ResponseCookie> invalidate(String credential);
}
