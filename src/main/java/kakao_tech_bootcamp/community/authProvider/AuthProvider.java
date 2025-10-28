package kakao_tech_bootcamp.community.authProvider;

import kakao_tech_bootcamp.community.entity.Member;

import java.util.List;

public interface AuthProvider {
    List<Credential> issue(Member member);

    Credential issueCredential(Member member);

    Credential issueRefreshCredential(Member member);

    AuthInfo validate(String credential, String refreshCredential);

    List<Credential> invalidate(String credential, String refreshCredential);

    Credential invalidateCredential(String credential);

    Credential invalidateRefreshCredential(String refreshCredential);
}
