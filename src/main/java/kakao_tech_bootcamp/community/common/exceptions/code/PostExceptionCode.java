package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionCode implements BaseExceptionCode {
    // 403 Forbidden
    FORBIDDEN_UPDATE(403_101, HttpStatus.FORBIDDEN, "게시글을 작성한 회원만 수정할 수 있습니다"),

    // 404 NotFound
    NOT_FOUND(404_101, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
