package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao_tech_bootcamp.community.common.exceptions.UnauthorizedException;
import kakao_tech_bootcamp.community.service.AuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthStrategy authStrategy;

    @Autowired
    public AuthInterceptor(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/auth".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            return true; // 로그인 요청만 공개 API
        }

        String credential = findCredential(request)
                .orElseThrow(() -> new UnauthorizedException("회원만 접근 가능한 서비스입니다"));

        authStrategy.validate(credential);

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
