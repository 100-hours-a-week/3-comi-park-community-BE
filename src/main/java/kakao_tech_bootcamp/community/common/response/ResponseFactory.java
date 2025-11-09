package kakao_tech_bootcamp.community.common.response;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ResponseFactory {
    public static ResponseEntity<CommonResponse<Void>> ok() {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed());
    }

    public static ResponseEntity<CommonResponse<Map<String,BaseResponse>>> ok(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed(wrapWithKey(dto)));
    }

    public static ResponseEntity<CommonResponse<Map<String,BaseResponse>>> created(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.succeed(wrapWithKey(dto)));
    }

    public static ResponseEntity<CommonResponse<Map<String,BaseResponse>>> updated(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed(wrapWithKey(dto)));
    }

    public static ResponseEntity<CommonResponse<Map<String,BaseResponse>>> deleted(BaseResponse dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.succeed(wrapWithKey(dto)));
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

    /*
    CommonResponse의 data 필드에 { "id": 1, "name": "test" }가 아니라
    "member": { "id": 1, "name": "test"}가 담기도록 변환하는 용도
     */
    private static Map<String, BaseResponse> wrapWithKey(BaseResponse dto) {
        String className = dto.getClass().getSimpleName();
        String key = extractKey(className);
        return Map.of(key, dto);
    }

    // dto 클래스 이름 중 첫 번째 단어를 소문자로 변환해 반환
    private static String extractKey(String className) {
        int count = 2;  // 2번째로 나오는 대문자는 새로운 단어의 시작이기 때문
        int i;

        for (i = 0; i < className.length(); i++) {
            if (Character.isUpperCase(className.charAt(i))) {
                if (--count == 0) break;
            }
        }

        return className.substring(0, i).toLowerCase();
    }
}
