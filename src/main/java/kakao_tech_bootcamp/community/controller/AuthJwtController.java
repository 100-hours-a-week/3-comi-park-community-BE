package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.jwt.JwtProperties;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final JwtProperties jwtProperties;
    private final AuthJwtService authJwtService;

    @Value("${cookie.http-only}")
    private static boolean httpOnly;
    @Value("${cookie.same-site}")
    private static String sameSite;
    @Value("${cookie.secure}")
    private static boolean secure;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        Map<String, String> credentials = authJwtService.issue(authRequestDto);

        ResponseCookie accessTokenCookie = createCookie(
                jwtProperties.getAccessToken().getKey(),
                credentials.get(jwtProperties.getAccessToken().getKey()),
                jwtProperties.getAccessToken().getPath(),
                jwtProperties.getAccessToken().getMaxAge()
        );
        ResponseCookie refreshTokenCookie = createCookie(
                jwtProperties.getRefreshToken().getKey(),
                credentials.get(jwtProperties.getRefreshToken().getKey()),
                jwtProperties.getRefreshToken().getPath(),
                jwtProperties.getRefreshToken().getMaxAge()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, accessTokenCookie.toString())
                .header(SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(value = "accessToken", required = false) String accessToken, @CookieValue("refreshToken") String refreshToken) {
        ResponseCookie accessTokenCookie = destroyCookie(jwtProperties.getAccessToken().getKey(), jwtProperties.getAccessToken().getPath());
        ResponseCookie refreshTokenCookie = destroyCookie(jwtProperties.getRefreshToken().getKey(), jwtProperties.getRefreshToken().getPath());

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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> reLogin(@CookieValue(value = "refreshToken") String refreshToken) {
        String accessToken = authJwtService.reIssue(refreshToken);
        ResponseCookie accessTokenCookie = createCookie(
                jwtProperties.getAccessToken().getKey(),
                accessToken,
                jwtProperties.getAccessToken().getPath(),
                jwtProperties.getAccessToken().getMaxAge()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(SET_COOKIE, accessTokenCookie.toString())
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
