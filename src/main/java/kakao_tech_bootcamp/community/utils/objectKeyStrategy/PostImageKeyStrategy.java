package kakao_tech_bootcamp.community.utils.objectKeyStrategy;

import kakao_tech_bootcamp.community.utils.ImageExtensionExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostImageKeyStrategy implements ObjectKeyStrategy{
    private final ImageExtensionExtractor extractor;

    @Override
    public String generate(String filename) {
        LocalDate now = LocalDate.now();
        return String.format("posts/%d/%02d/%s.%s",
                now.getYear(),
                now.getMonthValue(),
                UUID.randomUUID(),
                extractor.extract(filename)
        );
    }

    @Override
    public ImageCategory getCategory() {
        return ImageCategory.POST;
    }
}
