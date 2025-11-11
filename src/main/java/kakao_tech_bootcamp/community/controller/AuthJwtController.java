package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.common.CookieManager;
import kakao_tech_bootcamp.community.common.jwt.JwtProperties;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import kakao_tech_bootcamp.community.dto.request.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class AuthJwtController {
    private final JwtProperties jwtProperties;
    private final AuthJwtService authJwtService;
    private final CookieManager cookieManager;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>>login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        Map<String, String> credentials = authJwtService.issue(authRequestDto);

        ResponseCookie accessTokenCookie = cookieManager.createCookie(
                jwtProperties.getAccessToken().getKey(),
                credentials.get(jwtProperties.getAccessToken().getKey()),
                jwtProperties.getAccessToken().getPath(),
                jwtProperties.getAccessToken().getMaxAge()
        );
        ResponseCookie refreshTokenCookie = cookieManager.createCookie(
                jwtProperties.getRefreshToken().getKey(),
                credentials.get(jwtProperties.getRefreshToken().getKey()),
                jwtProperties.getRefreshToken().getPath(),
                jwtProperties.getRefreshToken().getMaxAge()
        );

        return ResponseFactory.created(List.of(accessTokenCookie, refreshTokenCookie));
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> logout(@CookieValue(value = "accessToken", required = false) String accessToken, @CookieValue("refreshToken") String refreshToken) {
        ResponseCookie accessTokenCookie = cookieManager.destroyCookie(jwtProperties.getAccessToken().getKey(), jwtProperties.getAccessToken().getPath());
        ResponseCookie refreshTokenCookie = cookieManager.destroyCookie(jwtProperties.getRefreshToken().getKey(), jwtProperties.getRefreshToken().getPath());

        return ResponseFactory.ok(List.of(accessTokenCookie, refreshTokenCookie));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue(value = "accessToken", required = false) String accessToken) {
        AuthInfo session = authJwtService.validate(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<Void>> reLogin(@CookieValue(value = "refreshToken") String refreshToken) {
        String accessToken = authJwtService.reIssue(refreshToken);
        ResponseCookie accessTokenCookie = cookieManager.createCookie(
                jwtProperties.getAccessToken().getKey(),
                accessToken,
                jwtProperties.getAccessToken().getPath(),
                jwtProperties.getAccessToken().getMaxAge()
        );

        return ResponseFactory.created(List.of(accessTokenCookie));
    }
}
