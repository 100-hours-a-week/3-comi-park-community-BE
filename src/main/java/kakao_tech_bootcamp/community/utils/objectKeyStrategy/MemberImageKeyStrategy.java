package kakao_tech_bootcamp.community.utils.objectKeyStrategy;

import kakao_tech_bootcamp.community.utils.ImageExtensionExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberImageKeyStrategy implements ObjectKeyStrategy {
    private final ImageExtensionExtractor extractor;

    @Override
    public String generate(String filename) {
        return "members/" + UUID.randomUUID() + "." + extractor.extract(filename);
    }

    @Override
    public ImageCategory getCategory() {
        return ImageCategory.MEMBER;
    }
}
