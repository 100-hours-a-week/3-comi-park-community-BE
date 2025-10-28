package kakao_tech_bootcamp.community.authProvider;

import kakao_tech_bootcamp.community.entity.Member;

import java.util.List;

public interface AuthProvider {
    List<Credential> issue(Member member);

    AuthInfo validate(String credential, String refreshCredential);

    List<Credential> invalidate(String credential, String refreshCredential);
}
