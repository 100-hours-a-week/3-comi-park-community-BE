package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.ImageExceptionCode;
import kakao_tech_bootcamp.community.dto.response.ImageResponseDto;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.repository.ImageRepository;
import kakao_tech_bootcamp.community.utils.ObjectKeyFactory;
import kakao_tech_bootcamp.community.utils.objectKeyStrategy.ImageCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ObjectKeyFactory objectKeyFactory;

    @Value("${storage.local-path}")
    private String localPath;
    @Value("${storage.base-url}")
    private String baseUrl;

    public ImageResponseDto saveImage(ImageCategory category, MultipartFile file) {
        String filename = file.getOriginalFilename();
        String objectKey = objectKeyFactory.create(category, filename);

        String filepath = createFilepath(objectKey);
        File destination = new File(filepath);
        destination.getParentFile().mkdirs();

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            log.error("[ImageUpload] 이미지 저장 중 IOException 발생, filename={}", filename, e);
            throw new CustomException(ImageExceptionCode.UPLOAD_ERROR);
        }

        Image image = new Image(filename, objectKey, baseUrl + objectKey);

        return ImageResponseDto.of(image);
    }

    public void removeImage(Integer imageId, String objectKey) {
        imageRepository.deleteById(imageId);

        String filepath = createFilepath(objectKey);
        File destination = new File(filepath);
        destination.delete(); // 물리적 이미지 파일 없거나 삭제 실패해도 오류 X
    }

    private String createFilepath(String objectKey) {
        return localPath + objectKey;
    }
}
