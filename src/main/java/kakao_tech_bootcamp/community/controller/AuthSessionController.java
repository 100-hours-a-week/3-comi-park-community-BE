package kakao_tech_bootcamp.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.CookieManager;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.common.session.SessionProperties;
import kakao_tech_bootcamp.community.dto.request.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/session")
@RequiredArgsConstructor
public class AuthSessionController {
    private final SessionProperties sessionProperties;
    private final AuthSessionService authSessionService;
    private final CookieManager cookieManager;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> login(
            HttpServletRequest request,
            @RequestBody @Validated AuthRequestDto authRequestDto) {
        String userAgent = request.getHeader("user-agent");
        Map<String, String> credentials = authSessionService.issue(authRequestDto, userAgent);

        ResponseCookie sid = cookieManager.createCookie(
                sessionProperties.getSessionId().getKey(),
                credentials.get(sessionProperties.getSessionId().getKey()),
                sessionProperties.getSessionId().getPath(),
                sessionProperties.getSessionId().getMaxAge()
        );
        ResponseCookie rid = cookieManager.createCookie(
                sessionProperties.getRefreshId().getKey(),
                credentials.get(sessionProperties.getRefreshId().getKey()),
                sessionProperties.getRefreshId().getPath(),
                sessionProperties.getRefreshId().getMaxAge()
        );

        return ResponseFactory.created(List.of(sid, rid));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> logout(
            @CookieValue(value = "sid", required = false) String sessionId,
            @CookieValue(value = "rid") String refreshId) {

        if (sessionId != null) {
            authSessionService.invalidate(sessionId);
        }

        ResponseCookie sid =  cookieManager.destroyCookie(sessionProperties.getSessionId().getKey(), sessionProperties.getSessionId().getPath());
        ResponseCookie rid = cookieManager.destroyCookie(sessionProperties.getRefreshId().getKey(), sessionProperties.getRefreshId().getPath());

        return ResponseFactory.ok(List.of(sid, rid));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("sid") String sessionId) {
        AuthInfo session = authSessionService.validate(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<Void>> reLogin(
            HttpServletRequest request,
            @CookieValue(value = "rid") String refreshId) {
        String userAgent = request.getHeader("user-agent");
        String sessionId = authSessionService.reIssue(refreshId, userAgent);

        ResponseCookie sid = cookieManager.createCookie(
                sessionProperties.getSessionId().getKey(),
                sessionId,
                sessionProperties.getSessionId().getPath(),
                sessionProperties.getSessionId().getMaxAge()
        );

        return ResponseFactory.created(List.of(sid));
    }
}
