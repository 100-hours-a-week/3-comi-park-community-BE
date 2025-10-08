package kakao_tech_bootcamp.community.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // 새 게시글 생성 시 인메모리DB에 올릴 때 사용
@AllArgsConstructor // RDB에서 count하고 인메모리DB에 올릴 때 사용
public class PostStat {
    private final Integer postId;
    private int viewCount;
    private int likeCount;
    private int commentCount;

    public void incrementViewCount() {
        viewCount++;
    }

    public void incrementLikeCount() {
        likeCount++;
    }

    public void decrementLikeCount() {
        likeCount--;
    }

    public void incrementCommentCount() {
        commentCount++;
    }

    public void decrementCommentCount() {
        commentCount--;
    }

    // member_post_like 테이블 count 후 인메모리DB의 통계값 수정용 메서드
    public void changeLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    // comment 테이블 count 후 인메모리DB의 통계값 수정용 메서드
    public void changeCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}