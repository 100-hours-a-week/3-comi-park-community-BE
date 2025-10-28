package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.authProvider.Credential;
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
        List<Credential> credentials = authService.issue(authRequestDto);
        return response(credentials);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue("credential") String credential,
                                                    @CookieValue(value = "refreshCredential") String refreshCredential) {
        List<Credential> credentials = authService.invalidate(credential, refreshCredential);
        return response(credentials);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, AuthInfo>>> validate(@CookieValue("credential") String credential,
                                                                       @CookieValue(value = "refreshCredential") String refreshCredential) {
        AuthInfo authInfo = authService.validate(credential, refreshCredential);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(Map.of("auth", authInfo)));
    }

    private ResponseEntity<ApiResponse<Void>> response(List<Credential> credentials) {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.CREATED);

        for (Credential credential : credentials) {
            responseBuilder.header(SET_COOKIE, createCookie(credential).toString());
        }

        return responseBuilder.body(ApiResponse.success());
    }

    private ResponseCookie createCookie(Credential credential) {
        return ResponseCookie.from(credential.getName(), credential.getValue())
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path(credential.getPath())
                .maxAge(credential.getTtlSeconds())
                .build();
    }
}
