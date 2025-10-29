package kakao_tech_bootcamp.community.controller;

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

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        String sessionId = authSessionService.issue(authRequestDto);
        ResponseCookie cookie = ResponseCookie.from("sid", sessionId)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7) // 일주일
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, cookie.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("sid") String sessionId) {
        authSessionService.invalidate(sessionId);

        ResponseCookie cookie = ResponseCookie.from("sid", sessionId)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, cookie.toString())
                .body(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("sid") String sessionId) {
        AuthInfo session = authSessionService.validate(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
    }
}
