package kakao_tech_bootcamp.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.session.SessionProperties;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/auth/session")
@RequiredArgsConstructor
public class AuthSessionController {
    private final SessionProperties sessionProperties;
    private final AuthSessionService authSessionService;

    @Value("${cookie.http-only}")
    private static boolean httpOnly;
    @Value("${cookie.same-site}")
    private static String sameSite;
    @Value("${cookie.secure}")
    private static boolean secure;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(
            HttpServletRequest request,
            @RequestBody @Validated AuthRequestDto authRequestDto) {
        String userAgent = request.getHeader("user-agent");
        Map<String, String> credentials = authSessionService.issue(authRequestDto, userAgent);

        ResponseCookie sid = createCookie(
                sessionProperties.getSessionId().getKey(),
                credentials.get(sessionProperties.getSessionId().getKey()),
                sessionProperties.getSessionId().getPath(),
                sessionProperties.getSessionId().getMaxAge()
        );
        ResponseCookie rid = createCookie(
                sessionProperties.getRefreshId().getKey(),
                credentials.get(sessionProperties.getRefreshId().getKey()),
                sessionProperties.getRefreshId().getPath(),
                sessionProperties.getRefreshId().getMaxAge()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, sid.toString())
                .header(SET_COOKIE, rid.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(value = "sid", required = false) String sessionId,
            @CookieValue(value = "rid") String refreshId) {

        if (sessionId != null) {
            authSessionService.invalidate(sessionId);
        }

        ResponseCookie sid = destroyCookie(sessionProperties.getSessionId().getKey(), sessionProperties.getSessionId().getPath());
        ResponseCookie rid = destroyCookie(sessionProperties.getRefreshId().getKey(), sessionProperties.getRefreshId().getPath());

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, sid.toString())
                .header(SET_COOKIE, rid.toString())
                .body(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("sid") String sessionId) {
        AuthInfo session = authSessionService.validate(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> reLogin(
            HttpServletRequest request,
            @CookieValue(value = "rid") String refreshId) {
        String userAgent = request.getHeader("user-agent");
        String sessionId = authSessionService.reIssue(refreshId, userAgent);

        ResponseCookie sid = createCookie(
                sessionProperties.getSessionId().getKey(),
                sessionId,
                sessionProperties.getSessionId().getPath(),
                sessionProperties.getSessionId().getMaxAge()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, sid.toString())
                .body(ApiResponse.success());
    }

    private ResponseCookie createCookie(String key, String value, String path, long maxAge) {
        return ResponseCookie.from(key, value)
                .httpOnly(httpOnly)
                .sameSite(sameSite)
                .secure(secure)
                .path(path)
                .maxAge(maxAge)
                .build();
    }

    private ResponseCookie destroyCookie(String key, String path) {
        return createCookie(key, "", path, 0);
    }
}
