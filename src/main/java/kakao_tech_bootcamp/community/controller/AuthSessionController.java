package kakao_tech_bootcamp.community.controller;

import jakarta.servlet.http.HttpServletRequest;
import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthSessionService;
import lombok.RequiredArgsConstructor;
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
    private final AuthSessionService authSessionService;

    private final static String SID_PATH = "/";
    private final static String RID_PATH = "/auth/session";

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(
            HttpServletRequest request,
            @RequestBody @Validated AuthRequestDto authRequestDto) {
        String userAgent = request.getHeader("user-agent");
        Map<String, String> credentials = authSessionService.issue(authRequestDto, userAgent);

        ResponseCookie sid = createCookie("sid", credentials.get("sessionId"), SID_PATH, 60 * 15); // 15분
        ResponseCookie rid = createCookie("rid", credentials.get("refreshId"), RID_PATH, 60 * 60 * 24 * 20); // 일주일

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

        ResponseCookie sid = createCookie("sid", sessionId, SID_PATH, 0);
        ResponseCookie rid = createCookie("rid", refreshId, RID_PATH, 0);
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

        ResponseCookie sid = createCookie("sid", sessionId, SID_PATH, 60 * 15); // 15분

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, sid.toString())
                .body(ApiResponse.success());
    }

    private ResponseCookie createCookie(String key, String value, String path, long maxAge) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path(path)
                .maxAge(maxAge)
                .build();
    }
}
