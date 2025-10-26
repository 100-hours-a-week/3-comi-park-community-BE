package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Map<String, String> PUBLIC_ENDPOINT = Map.of(
            "/auth", "POST",
            "/members", "POST",
            "/members/availability/email", "POST",
            "/members/availability/nickname", "POST",
            "/images/members", "POST",
            "/terms", "GET",
            "/privacy", "GET",
            "/style.css", "GET",
            "/header.css", "GET"
    );
    private final AuthStrategy authStrategy;

    @Autowired
    public AuthInterceptor(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equals(method)) {
            return true;
        }

        if (uri.startsWith("/s3/members")) {
            return true;
        }

        if (PUBLIC_ENDPOINT.containsKey(uri) && PUBLIC_ENDPOINT.get(uri).equals(method)) {
            return true;
        }

        String credential = findCredential(request)
                .orElseThrow(() -> new UnauthorizedException("회원만 접근 가능한 서비스입니다"));

        AuthInfo authInfo = authStrategy.validate(credential);
        request.setAttribute("LOGIN_MEMBER", authInfo);

        return true;
    }

    private Optional<String> findCredential(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("sid")) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        // 다른 인증 방법 사용 시 Authorization 헤더 처리 코드 필요

        return Optional.empty();
    }
}
