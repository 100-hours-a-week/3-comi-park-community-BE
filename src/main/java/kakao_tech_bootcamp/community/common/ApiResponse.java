package kakao_tech_bootcamp.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success()  {
        return new ApiResponse<>(true, ApiMessage.SUCCESS.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(T data)  {
        return new ApiResponse<>(true, ApiMessage.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> created(T data)  {
        return new ApiResponse<>(true, ApiMessage.CREATED.getMessage(), data);
    }

    public static <T> ApiResponse<T> modified(T data)  {
        return new ApiResponse<>(true, ApiMessage.MODIFIED.getMessage(), data);
    }

    public static <T> ApiResponse<T> removed(T data)  {
        return new ApiResponse<>(true, ApiMessage.REMOVED.getMessage(), data);
    }

    public static <T> ApiResponse<T> badRequest(T data) {
        return new ApiResponse<>(false, ApiMessage.BAD_REQUEST.getMessage(), data);
    }

    public static <T> ApiResponse<T> unauthorized(T data) {
        return new ApiResponse<>(false, ApiMessage.UNAUTHORIZED.getMessage(), data);
    }

    public static <T> ApiResponse<T> forbidden(T data) {
        return new ApiResponse<>(false, ApiMessage.FORBIDDEN.getMessage(), data);
    }

    public static <T> ApiResponse<T> notFound(T data) {
        return new ApiResponse<>(false, ApiMessage.NOT_FOUND.getMessage(), data);
    }

    public static <T> ApiResponse<T> conflict(T data) {
        return new ApiResponse<>(false, ApiMessage.CONFLICT.getMessage(), data);
    }
}
