package kakao_tech_bootcamp.community.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {
    @Value("${cookie.http-only}")
    private static boolean httpOnly;
    @Value("${cookie.same-site}")
    private static String sameSite;
    @Value("${cookie.secure}")
    private static boolean secure;

    public ResponseCookie createCookie(String key, String value, String path, long maxAge) {
        return ResponseCookie.from(key, value)
                .httpOnly(httpOnly)
                .sameSite(sameSite)
                .secure(secure)
                .path(path)
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie destroyCookie(String key, String path) {
        return createCookie(key, "", path, 0);
    }
}
