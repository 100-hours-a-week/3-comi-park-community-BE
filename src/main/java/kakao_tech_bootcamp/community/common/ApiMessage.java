package kakao_tech_bootcamp.community.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiMessage {
    SUCCESS("ok"),
    CREATED("create_success"),
    MODIFIED("modify_success"),
    REMOVED("remove_success"),
    BAD_REQUEST("invalid_request"),
    UNAUTHORIZED("not_authenticated"),
    FORBIDDEN("not_authorized"),
    NOT_FOUND("not_found"),
    CONFLICT("conflict_request");

    private final String message;
}
