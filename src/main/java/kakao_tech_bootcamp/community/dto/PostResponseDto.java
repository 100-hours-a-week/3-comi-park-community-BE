package kakao_tech_bootcamp.community.dto;

import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.entity.PostStat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Integer id;
    private String title;
    private String content;
    private MemberReferenceDto member;
    private ImageReferenceDto image;
    private LocalDateTime createdAt;
//    private boolean isLiked;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    public static PostResponseDto of(Post post, PostStat postStat) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(),
                MemberReferenceDto.of(post.getMember()),
                ImageReferenceDto.of(post.getImage()),
                post.getCreatedAt(),
                postStat.getViewCount(), postStat.getLikeCount(), postStat.getCommentCount());
    }
}
