package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.common.ApiResponse;
import kakao_tech_bootcamp.community.dto.AuthRequestDto;
import kakao_tech_bootcamp.community.service.AuthInfo;
import kakao_tech_bootcamp.community.service.AuthStrategy;
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
    private final AuthStrategy authStrategy;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Validated AuthRequestDto authRequestDto) {
        List<ResponseCookie> cookies = authStrategy.issue(authRequestDto);
        return response(cookies);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("sid") String sessionId) {
        List<ResponseCookie> cookies = authStrategy.invalidate(sessionId);
        return response(cookies);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("sid") String sessionId) {
        AuthInfo session = authStrategy.validate(sessionId);
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
