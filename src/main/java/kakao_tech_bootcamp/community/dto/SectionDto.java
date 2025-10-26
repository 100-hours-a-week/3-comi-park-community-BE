package kakao_tech_bootcamp.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SectionDto {
    private String title;
    private List<String> contents;
}
