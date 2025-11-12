package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements BaseExceptionCode {
    // 401 Unauthorized
    MISSING_AUTH(401_501, HttpStatus.UNAUTHORIZED, "인증 정보가 존재하지 않습니다"),
    EXPIRED_AUTH(401_502, HttpStatus.UNAUTHORIZED, "인증 정보가 만료되었습니다"),
    INVALID_AUTH(401_503, HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),

    // 403 Forbidden
    UNMATCHED_AUTH(403_501, HttpStatus.FORBIDDEN, "인증 정보와 회원 정보가 일치하지 않습니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
