package kakao_tech_bootcamp.community.common.response;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean success;
    private Integer code;
    private T data;

    public static <T> CommonResponse<T> succeed() {
        return new CommonResponse<>(true, null, null);
    }

    public static <T> CommonResponse<T> succeed(T data) {
        return new CommonResponse<>(true, null, data);
    }

    public static CommonResponse<List<String >> fail(List<String> data) {
        return new CommonResponse<>(false, null, data);
    }

    public static CommonResponse<String> fail(CustomException exception) {
        return new CommonResponse<>(
                false,
                exception.getExceptionCode().getExceptionCode(),
                exception.getExceptionCode().getMessage()
        );
    }
}
