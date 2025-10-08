package kakao_tech_bootcamp.community.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static kakao_tech_bootcamp.community.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long deleteAllByIsDeletedTrueAndDynamicFilters(Integer memberId, LocalDateTime before, LocalDateTime after) {
        return jpaQueryFactory.delete(post)
                .where(
                        post.isDeleted.isTrue(),
                        memberIdEq(memberId),
                        createdAtBetween(before, after)
                )
                .execute();
    }

    private Predicate createdAtBetween(LocalDateTime before, LocalDateTime after) {
        if (before != null && after != null) {
            return post.createdAt.between(after, before);
        }

        if (before != null) {
            return post.createdAt.before(before);
        }

        if (after != null) {
            return post.createdAt.after(after);
        }

        return null;
    }

    private Predicate memberIdEq(Integer memberId) {
        return memberId != null ? post.member.id.eq(memberId) : null;
    }
}
