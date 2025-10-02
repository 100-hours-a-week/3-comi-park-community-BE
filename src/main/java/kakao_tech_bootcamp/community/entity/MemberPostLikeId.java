package kakao_tech_bootcamp.community.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class MemberPostLikeId {
    private Integer postId;
    private Integer memberId;

    public MemberPostLikeId(Integer postId, Integer memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }
}
