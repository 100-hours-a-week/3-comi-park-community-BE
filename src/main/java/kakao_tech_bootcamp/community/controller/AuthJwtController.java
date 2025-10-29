package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Controller
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class AuthJwtController {
    private final AuthJwtService authJwtService;

    private final static String ACCESS_TOKEN_PATH = "/";
    private final static String REFRESH_TOKEN_PATH = "/auth/jwt";

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        Map<String, String> credentials = authJwtService.issue(authRequestDto);

        ResponseCookie accessTokenCookie = createCookie("accessToken", credentials.get("accessToken"), ACCESS_TOKEN_PATH, 60 * 15); // 15분
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", credentials.get("refreshToken"), REFRESH_TOKEN_PATH, 60 * 60 * 24 * 30); // 30일

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(value = "accessToken", required = false) String accessToken, @CookieValue("refreshToken") String refreshToken) {
        ResponseCookie accessTokenCookie = createCookie("accessToken", accessToken, ACCESS_TOKEN_PATH, 0);
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", refreshToken, REFRESH_TOKEN_PATH, 0);

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue(value = "accessToken", required = false) String accessToken) {
        AuthInfo session = authJwtService.validate(accessToken);
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
