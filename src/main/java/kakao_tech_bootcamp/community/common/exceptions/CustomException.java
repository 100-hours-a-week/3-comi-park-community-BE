package kakao_tech_bootcamp.community.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final BaseExceptionCode exceptionCode;
}
