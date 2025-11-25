package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageExceptionCode implements BaseExceptionCode {
    // 400 BadRequest
    UNSUPPORTED_EXTENSION(400_401, HttpStatus.BAD_REQUEST, "이미지 파일(png, jpg, jpeg, webp, heic, heif)만 업로드할 수 있습니다"),
    UNSUPPORTED_CATEGORY(400_402, HttpStatus.BAD_REQUEST, "해당 카테고리로의 이미지 업로드를 지원하지 않습니다"),

    // 404 NotFound
    NOT_FOUND(404_401, HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다"),

    // 500
    UPLOAD_ERROR(500_501, HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 업로드하는 데 문제가 발생했습니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
