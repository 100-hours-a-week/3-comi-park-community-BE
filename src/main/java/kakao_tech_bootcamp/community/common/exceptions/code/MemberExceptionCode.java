package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements BaseExceptionCode {
    // 400 BadRequest
    UNMATCHED_PASSWORD(400_001, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),

    // 401 Unauthorized
    UNAUTHORIZED(401_001, HttpStatus.UNAUTHORIZED, "회원만 접근 가능한 서비스입니다"),

    // 403 Forbidden
    FORBIDDEN_UPDATE(403_001, HttpStatus.FORBIDDEN, "회원 본인 정보에 대해서만 수정할 수 있습니다"),
    FORBIDDEN_DELETE(403_002, HttpStatus.FORBIDDEN, "회원 본인만 탈퇴할 수 있습니다"),

    // 404 NotFound
    NOT_FOUND(404_001, HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),

    // 409 Conflict
    DUPLICATED_EMAIL(409_001, HttpStatus.CONFLICT, "중복된 이메일입니다"),
    DUPLICATED_NICKNAME(409_002, HttpStatus.CONFLICT, "중복된 닉네임입니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
