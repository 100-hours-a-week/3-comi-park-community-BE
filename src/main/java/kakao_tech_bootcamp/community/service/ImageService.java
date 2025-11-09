package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.StorageProperties;
import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.ImageExceptionCode;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.entity.ImageStatus;
import kakao_tech_bootcamp.community.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final static List<String> AVAILABLE_EXTENSIONS = List.of("png", "jpg", "jpeg", "png", "webp", "heic", "heif");
    private final static String REGEX = ".([a-zA-Z]+)$";

    private final ImageRepository imageRepository;

    public ImageResponseDto saveImage(String type, MultipartFile file) throws IOException{
        String filename = file.getOriginalFilename();

        String objectKey = createObjectKey(type, filename);
        Image image = new Image(filename, objectKey, ImageStatus.PENDING);
        Image saveImage = imageRepository.save(image);

        String filepath = StorageProperties.createFilepath(objectKey);
        File destination = new File(filepath);
        destination.getParentFile().mkdirs();
        file.transferTo(destination);

        return ImageResponseDto.of(image);
    }

    public Image modifyImageStatusById(Integer imageId, ImageStatus imageStatus) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(ImageExceptionCode.NOT_FOUND));
        image.changeStatus(imageStatus);
        // TODO: 엔티티 말고 dto를 반환할 방법?
        return image;
    }

    public void removeImage(Integer imageId, String objectKey) {
        imageRepository.deleteById(imageId);

        String filepath = StorageProperties.createFilepath(objectKey);
        File destination = new File(filepath);
        destination.delete(); // 물리적 이미지 파일 없거나 삭제 실패해도 오류 X
    }

    private String createObjectKey(String type, String filename) {
        Matcher matcher = Pattern.compile(REGEX).matcher(filename);

        if (!matcher.find()) {
            throw new CustomException(ImageExceptionCode.UNSUPPORTED_EXTENSION);
        }

        String extension = matcher.group(1);
        validateExtension(extension);

        StringBuffer sb = new StringBuffer();

        if ("posts".equals(type)) {
            LocalDate current = LocalDate.now();
            sb.append("posts/")
                    .append(current.getYear())
                    .append("/")
                    .append(current.getMonthValue())
                    .append("/")
                    .append(UUID.randomUUID().toString())
                    .append(".")
                    .append(extension);

            return sb.toString();
        }

        if ("members".equals(type)) {
            sb.append("members/")
                    .append(UUID.randomUUID().toString())
                    .append(".")
                    .append(extension);

            return sb.toString();
        }

        return "";
    }

    private void validateExtension(String extension) {
        if (!AVAILABLE_EXTENSIONS.contains(extension)) {
            throw new CustomException(ImageExceptionCode.UNSUPPORTED_EXTENSION);
        }
    }
}
