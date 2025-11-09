package kakao_tech_bootcamp.community.common.exceptions;

import org.springframework.http.HttpStatus;

public interface BaseExceptionCode {
    Integer getExceptionCode();
    HttpStatus getStatusCode();
    String getMessage();
}
