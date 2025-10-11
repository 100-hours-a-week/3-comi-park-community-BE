package kakao_tech_bootcamp.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostAllResponseDto {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;
    private MemberReferenceDto member;
    private boolean isLiked;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    @QueryProjection
    public PostAllResponseDto(
            Integer id,
            String title,
            LocalDateTime createdAt,
            MemberReferenceDto member,
            boolean isLiked,
            Integer viewCount,
            Integer likeCount,
            Integer commentCount
    ) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.member = member;
        this.isLiked = isLiked;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
