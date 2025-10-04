package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.BadRequestException;
import kakao_tech_bootcamp.community.dto.ImageResponseDto;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.entity.ImageStatus;
import kakao_tech_bootcamp.community.repository.ImageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Log4j2
@Service
@Transactional
public class ImageService {
    @Value("${storage.domain}")
    private String domain; // S3 사용한다고 가정해 dirPath 대신 domain 이름 설정
    private final List<String> AVAILABLE_EXTENSIONS = List.of("png", "jpg", "jpeg", "png", "webp", "heic", "heif");
    private final String REGEX = ".([a-zA-Z]+)$";

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ImageResponseDto saveImage(Integer currentMemberId, String type, MultipartFile file) throws IOException{
        String filename = file.getOriginalFilename();

        String objectKey = createObjectKey(currentMemberId, type, filename);
        Image image = new Image(filename, objectKey, ImageStatus.PENDING);
        Image saveImage = imageRepository.save(image);

        String filepath = createUrl(objectKey);
        File destination = new File(filepath);
        destination.getParentFile().mkdirs();
        file.transferTo(destination);

        ImageResponseDto imageResponseDto = new ImageResponseDto();
        imageResponseDto.setId(saveImage.getId());
        imageResponseDto.setObjectKey(objectKey);
        imageResponseDto.setFilename(filename);

        return imageResponseDto;
    }

    private String createObjectKey(Integer currentMemberId, String type, String filename) {
        Matcher matcher = Pattern.compile(REGEX).matcher(filename);

        if (!matcher.find()) {
            throw new BadRequestException("이미지 파일(png, jpg, jpeg, png, webp, heic, heif)만 업로드할 수 있습니다");
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
                    .append(currentMemberId)
                    .append("/")
                    .append(UUID.randomUUID().toString())
                    .append(".")
                    .append(extension);

            return sb.toString();
        }

        throw new BadRequestException("잘못된 요청입니다");
    }

    private void validateExtension(String extension) {
        if (!AVAILABLE_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("이미지 파일(png, jpg, jpeg, png, webp, heic, heif)만 업로드할 수 있습니다");
        }
    }

    private String createUrl(String objectKey) {
        return domain + objectKey;
    }
}
