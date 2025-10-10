package kakao_tech_bootcamp.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostAllResponseDto {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;
    private MemberReferenceDto member;
    private boolean isLiked;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    public static PostAllResponseDto of(Object[] row) {
        Integer id = ((Number) row[0]).intValue();
        String title = (String) row[1];
        LocalDateTime createdAt = ((Timestamp) row[2]).toLocalDateTime();
        String memberNickname = (String) row[3];
        Integer memberImageId = row[4] != null ? ((Number) row[4]).intValue() : null;
        String memberImageObjectKey = (String) row[5];
        boolean isLiked = ((Number) row[6]).longValue() == 1L;
        int viewCount = ((Number) row[7]).intValue();
        int likeCount = ((Number) row[8]).intValue();
        int commentCount = ((Number) row[9]).intValue();

        MemberReferenceDto member = MemberReferenceDto.of(memberNickname, memberImageId, memberImageObjectKey);

        return new PostAllResponseDto(id, title, createdAt, member, isLiked, viewCount, likeCount, commentCount);
    }
}
