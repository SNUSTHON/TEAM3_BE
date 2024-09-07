package com.team3.core.domain.challenge.dao;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team3.core.domain.challenge.dto.response.DayChallengeResponse;
import com.team3.core.domain.challenge.dto.response.RangeChallengeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.team3.core.domain.category.domain.QCategory.category;
import static com.team3.core.domain.challenge.domain.QChallenge.challenge;
import static com.team3.core.domain.challenge.domain.QMemberChallenge.memberChallenge;

@Repository
@RequiredArgsConstructor
public class MemberChallengeQueryRepository {

    private final JPAQueryFactory query;

//    public List<MyTodayChallengesResponse> findMyTodayChallenges(Long memberId) {
//        return query
//                .select(
//                        Projections.fields(MyTodayChallengesResponse.class,
//                                category.name.as("categoryName"),
//                                ExpressionUtils.as(
//                                        JPAExpressions
//                                                .select(Projections.fields(
//                                                        MyTodayChallengesResponse.MyTodayChallengeItem.class,
//                                                        memberChallenge.id.as("memberChallengesId"),
//                                                        challenge.name.as("challengeName"),
//                                                        challenge.level.as("level"),
//                                                        challenge.duration.as("duration"),
//                                                        memberChallenge.isDone.as("isDone")
//                                                ))
//                                                .from(memberChallenge)
//                                                .join(memberChallenge.challenge, challenge)
//                                                .where(category.name.eq(memberChallenge.challenge.category.name)),
//                                        "challenges"
//                                )
//                        )
//                )
//                .from(memberChallenge)
//                .innerJoin(memberChallenge.challenge, challenge)
//                .innerJoin(challenge.category, category)
//                .where(isMember(memberId))
//                .fetch();
//    }

    public List<DayChallengeResponse> findChallengesByDate(Long memberId, String dateFormatString) {
        return query
                .select(
                        Projections.fields(DayChallengeResponse.class,
                                challenge.name.as("challengeName"),
                                challenge.level.as("level"),
                                category.name.as("categoryName"),
                                challenge.duration.as("duration"),
                                memberChallenge.isDone.as("isDone")
                        )
                )
                .from(memberChallenge)
                .innerJoin(memberChallenge.challenge, challenge)
                .innerJoin(challenge.category, category)
                .where(isMember(memberId), getMemberChallengeDateFormat().eq(dateFormatString))
                .orderBy(category.name.asc())
                .fetch();
    }

    public List<RangeChallengeResponse> findChallengesCountByRange(Long memberId, LocalDateTime startAt, LocalDateTime endAt) {

        return query
                .select(
                        Projections.fields(RangeChallengeResponse.class,
                                getMemberChallengeDateFormat().as("createdAt"),
                                memberChallenge.id.count().intValue().as("count")
                        )
                )
                .from(memberChallenge)
                .where(isMember(memberId), isBetween(startAt, endAt))
                .groupBy(getMemberChallengeDateFormat())
                .orderBy(getMemberChallengeDateFormat().asc())
                .fetch();
    }

    private StringTemplate getMemberChallengeDateFormat() {
        return Expressions.stringTemplate("DATE_FORMAT({0}, '{1s}')", memberChallenge.createdAt, ConstantImpl.create("%Y-%m-%d"));
    }

    private static BooleanExpression isMember(Long memberId) {
        return memberChallenge.member.id.eq(memberId);
    }

    private static BooleanExpression isBetween(LocalDateTime startAt, LocalDateTime endAt) {
        return memberChallenge.createdAt.between(startAt, endAt);
    }
}
