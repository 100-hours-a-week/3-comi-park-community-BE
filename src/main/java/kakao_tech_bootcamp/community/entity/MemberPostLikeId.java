package kakao_tech_bootcamp.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class MemberPostLikeId {
    @Column(name = "post_id", nullable = false, updatable = false)
    private Integer postId;
    @Column(name = "member_id", nullable = false, updatable = false)
    private Integer memberId;

    public MemberPostLikeId(Integer postId, Integer memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }
}
