package kakao_tech_bootcamp.community.common.response;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

public class ResponseFactory {
    public static ResponseEntity<CommonResponse<Void>> ok() {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed());
    }

    public static ResponseEntity<CommonResponse<BaseResponse>> ok(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed(dto));
    }

    public static ResponseEntity<CommonResponse<Void>> ok(List<ResponseCookie> cookies) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
        cookies.forEach(cookie -> builder.header(SET_COOKIE, cookie.toString()));
        return builder.body(CommonResponse.succeed());
    }

    public static ResponseEntity<CommonResponse<BaseResponse>> created(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.succeed(dto));
    }

    public static ResponseEntity<CommonResponse<Void>> created(List<ResponseCookie> cookies) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
        cookies.forEach(cookie -> builder.header(SET_COOKIE, cookie.toString()));
        return builder.body(CommonResponse.succeed());
    }

    public static ResponseEntity<CommonResponse<String>> fail(CustomException exception) {
        return ResponseEntity
                .status(exception.getExceptionCode().getStatusCode())
                .body(CommonResponse.fail(exception));
    }

    public static ResponseEntity<CommonResponse<List<String>>> fail(List<String> errors) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(errors));
    }
}
