package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao_tech_bootcamp.community.authProvider.AuthProvider;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.authProvider.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthProvider authProvider;
    private static final Map<String, String> EXCLUDE_PATHS = Map.of(
            "/auth", "POST",
            "/members", "POST",
            "/members/availability/email", "POST",
            "/members/availability/nickname", "POST",
            "/images/members", "POST"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (shouldExcludePath(request)) {
            return true;
        }

        Map<String, String> credentials = extractCredential(request);

        if (credentials.isEmpty()) {
            throw new UnauthorizedException("회원만 접근 가능한 서비스입니다");
        }

        AuthInfo authInfo = authProvider.validate(credentials.get("credential"), credentials.get("refreshCredential"));
        request.setAttribute("LOGIN_MEMBER", authInfo);

        return true;
    }

    private boolean shouldExcludePath(HttpServletRequest request) {
        String currentUri = request.getRequestURI();
        String currentMethod = request.getMethod();

        if ("OPTIONS".equals(currentMethod)) {
            return true;
        }

        return EXCLUDE_PATHS.entrySet().stream()
                .anyMatch(entry ->
                        entry.getKey().equals(currentUri) && entry.getValue().equals(currentMethod)
                );
    }

    private Map<String, String> extractCredential(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().startsWith("credential"))
                .collect(Collectors.toMap(
                        Cookie::getName,
                        Cookie::getValue
                ));
    }
}
