package kakao_tech_bootcamp.community.utils;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.ImageExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ImageExtensionExtractor {
    @Value("${allowed-image-extensions}")
    private Set<String> allowed;

    public String extract(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        if (!allowed.contains(extension)) {
            throw new CustomException(ImageExceptionCode.UNSUPPORTED_EXTENSION);
        }

        return extension;
    }
}
