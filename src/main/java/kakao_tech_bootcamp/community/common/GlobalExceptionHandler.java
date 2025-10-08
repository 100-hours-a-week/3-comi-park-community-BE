package kakao_tech_bootcamp.community.common;

import kakao_tech_bootcamp.community.common.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("invalid_request", errors));
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> badRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("invalid_request", e.getMessage()));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> unauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("not_authenticated", e.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ApiResponse<String>> forbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>("not_authorized", e.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> notFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("not_found", e.getMessage()));
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ApiResponse<String>> conflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>("conflict_request", e.getMessage()));
    }
}
