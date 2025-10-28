package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.authProvider.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody @Validated AuthRequestDto authRequestDto) {
        List<ResponseCookie> cookies = authService.issue(authRequestDto);
        return response(cookies);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("credential") String credential) {
        List<ResponseCookie> cookies = authService.invalidate(credential);
        return response(cookies);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("credential") String credential) {
        AuthInfo session = authService.validate(credential);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", session)));
    }

    private ResponseEntity<ApiResponse<Void>> response(List<ResponseCookie> cookies) {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.CREATED);

        for (ResponseCookie cookie : cookies) {
            responseBuilder.header(SET_COOKIE, cookie.toString());
        }

        return responseBuilder.body(ApiResponse.success());
    }
}
