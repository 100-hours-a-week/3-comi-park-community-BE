package kakao_tech_bootcamp.community.common.exceptions.code;

import kakao_tech_bootcamp.community.common.exceptions.BaseExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageExceptionCode implements BaseExceptionCode {
    // 400 BadRequest
    UNSUPPORTED_EXTENSION(400_401, HttpStatus.BAD_REQUEST, "이미지 파일(png, jpg, jpeg, png, webp, heic, heif)만 업로드할 수 있습니다"),

    // 404 NotFound
    NOT_FOUND(404_401, HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다");

    private final Integer exceptionCode;
    private final HttpStatus statusCode;
    private final String message;
}
