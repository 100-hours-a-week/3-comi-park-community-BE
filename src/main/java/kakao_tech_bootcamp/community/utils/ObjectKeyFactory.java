package kakao_tech_bootcamp.community.utils;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.ImageExceptionCode;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ImageCategory;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ObjectKeyStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ObjectKeyFactory {
    private final List<ObjectKeyStrategy> strategies;

    public String create(ImageCategory category, String filename) {
        return strategies.stream()
                .filter(s -> s.getCategory() == category)
                .findFirst()
                .orElseThrow(() -> new CustomException(ImageExceptionCode.UNSUPPORTED_CATEGORY))
                .generate(filename);
    }
}
