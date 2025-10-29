package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import kakao_tech_bootcamp.community.service.AuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private static final Map<String, String> PUBLIC_ENDPOINT = Map.of(
            "/members", "POST",
            "/members/availability/email", "POST",
            "/members/availability/nickname", "POST",
            "/images/members", "POST"
    );
    private final AuthStrategy authSessionService;
    private final AuthJwtService authJwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equals(method)) {
            return true;
        }

        if (PUBLIC_ENDPOINT.containsKey(uri) && PUBLIC_ENDPOINT.get(uri).equals(method)) {
            return true;
        }

        AuthInfo authInfo = extractCredential(request, "sid")
                .map(authSessionService::validate)
                .or(() -> extractCredential(request, "accessToken").map(authJwtService::validate))
                .orElseThrow(() -> new UnauthorizedException("회원만 접근 가능한 서비스입니다"));
        request.setAttribute("LOGIN_MEMBER", authInfo);

        return true;
    }

    private Optional<String> extractCredential(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
