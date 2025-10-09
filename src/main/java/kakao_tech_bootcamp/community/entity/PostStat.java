package kakao_tech_bootcamp.community.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "post_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostStat {
    @Id
    @Column(name = "post_id")
    private Integer postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY) // Lazy loading 잘 되는지 확인
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "view_count", nullable = false)
    @ColumnDefault("0")
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    @Column(name = "comment_count", nullable = false)
    @ColumnDefault("0")
    private int commentCount;

    public PostStat(Post post) {
        this.post = post;
    }

    public PostStat(Post post, int likeCount, int commentCount) {
        this.post = post;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

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

    // member_post_like 테이블 count 후 통계값 수정용 메서드
    public void changeLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    // comment 테이블 count 후 통계값 수정용 메서드
    public void changeCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}