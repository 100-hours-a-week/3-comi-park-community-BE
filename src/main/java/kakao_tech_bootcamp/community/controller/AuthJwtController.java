package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class AuthJwtController {
    private final AuthJwtService authJwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        Map<String, String> credentials = authJwtService.issue(authRequestDto);

        ResponseCookie accessTokenCookie = createCookie("accessToken", credentials.get("accessToken"), "/", 60 * 15); // 15분
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", credentials.get("refreshToken"), "/", 60 * 60 * 24 * 30); // 30일

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("accessToken") String accessToken, @CookieValue("refreshToken") String refreshToken) {
        ResponseCookie accessTokenCookie = createCookie("accessToken", accessToken, "/", 0); // 15분
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", refreshToken, "/", 0); // 30일

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue(value = "accessToken", required = false) String accessToken,
                                                                       @CookieValue("refreshToken") String refreshToken) {
        AuthInfo session = authJwtService.validate(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
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
