package kakao_tech_bootcamp.community.common;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.response.CommonResponse;
import kakao_tech_bootcamp.community.common.response.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<List<String>>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
        return ResponseFactory.fail(errors);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<CommonResponse<String>> handleCustomException(CustomException e) {
        return ResponseFactory.fail(e);
    }
}
