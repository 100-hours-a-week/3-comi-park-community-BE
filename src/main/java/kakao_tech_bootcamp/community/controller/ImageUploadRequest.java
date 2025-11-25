package kakao_tech_bootcamp.community.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ImageUploadRequest {
    private MultipartFile file;
    private String filename;
}
