package kakao_tech_bootcamp.community.dto.response.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.MemberReferenceDto;
import kakao_tech_bootcamp.community.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto implements BaseResponse {
    private Integer id;
    private String title;
    private String content;
    private MemberReferenceDto member;
    private ImageDto image;
    private LocalDateTime createdAt;

    private Boolean postDeleted;

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .member(MemberReferenceDto.of(post.getMember()))
                .image(ImageDto.of(post.getImage()))
                .createdAt(post.getCreatedAt())
                .build();
    }
}
