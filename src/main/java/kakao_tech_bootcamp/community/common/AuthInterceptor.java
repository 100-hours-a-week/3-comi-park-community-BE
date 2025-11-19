package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberExceptionCode;
import kakao_tech_bootcamp.community.config.PublicEndpointProperties;
import kakao_tech_bootcamp.community.dto.response.AuthResponseDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import kakao_tech_bootcamp.community.service.AuthSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final PublicEndpointProperties properties;
    private final AuthSessionService authSessionService;
    private final AuthJwtService authJwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ("OPTIONS".equals(method)) {
            return true;
        }

        for (PublicEndpointProperties.EndPoint endPoint : properties.getPublicEndpoints()) {
            if (endPoint.getPath().equals(uri) && endPoint.getMethod().equals(method)) {
                return true;
            }
        }

        AuthResponseDto authInfo = extractCredential(request, "sid")
                .map(authSessionService::validate)
                .or(() -> extractCredential(request, "accessToken").map(authJwtService::validate))
                .orElseThrow(() -> new CustomException(MemberExceptionCode.UNAUTHORIZED));

        request.setAttribute("LOGIN_MEMBER", new AuthInfo(authInfo.getAuth().getId()));

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
