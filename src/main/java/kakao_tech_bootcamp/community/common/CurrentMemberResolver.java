package kakao_tech_bootcamp.community.common;

import jakarta.servlet.http.HttpServletRequest;
import kakao_tech_bootcamp.community.common.annotation.CurrentMember;
import kakao_tech_bootcamp.community.service.AuthInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentMemberResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @CurrentMember AuthInfo {변수} 형식의 파라미터만 해석하겠다는 의미
        return parameter.hasParameterAnnotation(CurrentMember.class) && parameter.getParameterType().equals(AuthInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest)  webRequest.getNativeRequest();
        return request.getAttribute("LOGIN_MEMBER");
    }
}
