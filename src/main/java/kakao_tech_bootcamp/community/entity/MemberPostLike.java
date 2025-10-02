package kakao_tech_bootcamp.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_post_like")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MemberPostLike {
    @EmbeddedId
    private MemberPostLikeId memberPostLikeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public MemberPostLike(Post post, Member member) {
        memberPostLikeId = new MemberPostLikeId(post.getId(), member.getId());
    }
}
