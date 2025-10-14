package kakao_tech_bootcamp.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;

    public static <T> ApiResponse<T> success()  {
        return new ApiResponse<>(ApiMessage.SUCCESS.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(T data)  {
        return new ApiResponse<>(ApiMessage.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> created(T data)  {
        return new ApiResponse<>(ApiMessage.CREATED.getMessage(), data);
    }

    public static <T> ApiResponse<T> modified(T data)  {
        return new ApiResponse<>(ApiMessage.MODIFIED.getMessage(), data);
    }

    public static <T> ApiResponse<T> removed(T data)  {
        return new ApiResponse<>(ApiMessage.REMOVED.getMessage(), data);
    }

    public static <T> ApiResponse<T> badRequest(T data) {
        return new ApiResponse<>(ApiMessage.BAD_REQUEST.getMessage(), data);
    }

    public static <T> ApiResponse<T> unauthorized(T data) {
        return new ApiResponse<>(ApiMessage.UNAUTHORIZED.getMessage(), data);
    }

    public static <T> ApiResponse<T> forbidden(T data) {
        return new ApiResponse<>(ApiMessage.FORBIDDEN.getMessage(), data);
    }

    public static <T> ApiResponse<T> notFound(T data) {
        return new ApiResponse<>(ApiMessage.NOT_FOUND.getMessage(), data);
    }

    public static <T> ApiResponse<T> conflict(T data) {
        return new ApiResponse<>(ApiMessage.CONFLICT.getMessage(), data);
    }
}
