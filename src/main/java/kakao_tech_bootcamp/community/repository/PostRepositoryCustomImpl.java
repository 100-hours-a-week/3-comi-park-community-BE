package kakao_tech_bootcamp.community.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kakao_tech_bootcamp.community.dto.PostAllResponseDto;
import kakao_tech_bootcamp.community.dto.QPostAllResponseDto;
import kakao_tech_bootcamp.community.dto.QMemberReferenceDto;
import kakao_tech_bootcamp.community.dto.QImageReferenceDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static kakao_tech_bootcamp.community.entity.QImage.image;
import static kakao_tech_bootcamp.community.entity.QMember.member;
import static kakao_tech_bootcamp.community.entity.QMemberPostLike.memberPostLike;
import static kakao_tech_bootcamp.community.entity.QPost.post;
import static kakao_tech_bootcamp.community.entity.QPostStat.postStat;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostAllResponseDto> findAllIdLessThanCustom(Integer memberId, Integer lastPostId, Integer limit) {
        Expression<Boolean> isLikedExpression = JPAExpressions
                .selectOne()
                .from(memberPostLike)
                .where(memberPostLike.memberPostLikeId.postId.eq(post.id)
                        .and(memberPostLike.memberPostLikeId.memberId.eq(memberId)))
                .exists();

        JPAQuery<PostAllResponseDto> query = jpaQueryFactory
                .select(
                        new QPostAllResponseDto(
                                post.id,
                                post.title,
                                post.createdAt,
                                new QMemberReferenceDto(
                                        member.id,
                                        member.nickname,
                                        new QImageReferenceDto(
                                                image.id,
                                                image.objectKey
                                        )
                                ),
                                isLikedExpression,
                                postStat.viewCount,
                                postStat.likeCount,
                                postStat.commentCount
                        )
                )
                .from(post)
                .join(member).on(member.id.eq(post.member.id))
                .join(postStat).on(postStat.postId.eq(post.id))
                .leftJoin(image).on(image.id.eq(member.image.id))
                .where(post.isDeleted.eq(false)
                        .and(lastPostId == null ? null : post.id.lt(lastPostId)))
                .orderBy(post.id.desc())
                .limit(limit);

        return query.fetch();
    }

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
