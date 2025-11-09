package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberLikeExceptionCode implements BaseExceptionCode {
    // 404 NotFound
    NOT_FOUND(404_201, HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다"),

    // 409 Conflict
    DUPLICATED_LIKE(409_201, HttpStatus.CONFLICT, "회원이 이미 좋아요한 게시글입니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
