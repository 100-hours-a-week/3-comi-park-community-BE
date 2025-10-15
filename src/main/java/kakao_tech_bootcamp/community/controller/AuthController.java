package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthStrategy authStrategy;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Validated AuthRequestDto authRequestDto) {
        String sessionId = authStrategy.issue(authRequestDto);
        ResponseCookie cookie = ResponseCookie.from("sid", sessionId)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(60 * 60 * 24 * 7) // 일주일
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, cookie.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("sid") String sessionId) {
        authStrategy.invalidate(sessionId);
        ResponseCookie cookie = ResponseCookie.from("sid", sessionId).maxAge(0).build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(SET_COOKIE, cookie.toString())
                .build();
    }
}
