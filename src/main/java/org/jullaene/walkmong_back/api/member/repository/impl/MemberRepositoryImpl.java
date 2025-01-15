package org.jullaene.walkmong_back.api.member.repository.impl;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.apply.domain.QApply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.api.member.dto.common.WalkingBasicInfo;
import org.jullaene.walkmong_back.api.member.dto.res.MyInfoResponseDto;
import org.jullaene.walkmong_back.api.member.repository.MemberRepositoryCustom;
import org.jullaene.walkmong_back.api.review.domain.QReviewToOwner;
import org.jullaene.walkmong_back.api.review.domain.QReviewToWalker;

import static com.querydsl.core.types.ExpressionUtils.count;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public MyInfoResponseDto getMemberInfo(Long memberId, String delYn) {
        QMember member = QMember.member;
        QAddress address = QAddress.address;

        return queryFactory.select(
                Projections.constructor(
                        MyInfoResponseDto.class,
                        member.nickname,
                        address.addressId,
                        address.dongAddress,
                        member.introduce,
                        member.name,
                        member.gender,
                        member.birthDate,
                        member.phone,
                        member.profile
                )
        )
                .from(member)
                .join(address)
                .on(address.memberId.eq(memberId)
                        .and(address.basicAddressYn.eq("Y"))
                        .and(address.delYn.eq(delYn)))
                .where(member.memberId.eq(memberId)
                        .and(member.delYn.eq(delYn)))
                .fetchOne();
    }

    @Override
    public WalkingBasicInfo getWalkingInfo(Long memberId, String delYn) {
        QMember member = QMember.member;
        QAddress address = QAddress.address;
        QApply apply = QApply.apply;
        QReviewToOwner reviewToOwner = QReviewToOwner.reviewToOwner;
        QReviewToWalker reviewToWalker = QReviewToWalker.reviewToWalker;

        return queryFactory
                .select(
                        Projections.constructor(
                                WalkingBasicInfo.class,
                                member.nickname,
                                address.dongAddress,
                                member.introduce,
                                member.name,
                                member.gender,
                                member.birthDate,
                                member.phone,
                                member.profile,
                                member.dogOwnership,
                                count(apply),
                                member.availabilityWithSize,
                                count(reviewToWalker),
                                calculateAverage(reviewToWalker, reviewToWalker.photoSharing),
                                calculateAverage(reviewToWalker, reviewToWalker.attitude),
                                calculateAverage(reviewToWalker, reviewToWalker.taskCompletion),
                                calculateAverage(reviewToWalker, reviewToWalker.timePunctuality),
                                calculateAverage(reviewToWalker, reviewToWalker.communication),
                                count(reviewToOwner),
                                Expressions.numberTemplate(Integer.class,
                                        "CASE WHEN count({0}) > 0 THEN (count(CASE WHEN {1} = 'Y' THEN 1 END) * 100.0) / count({0}) ELSE 0 END",
                                        reviewToOwner, reviewToOwner.goodYn)
                        )
                )
                .from(member)
                .leftJoin(apply)
                .on(apply.memberId.eq(memberId)
                        .and(apply.matchingStatus.eq(MatchingStatus.CONFIRMED))
                        .and(apply.delYn.eq(delYn)))
                .leftJoin(reviewToOwner)
                .on(reviewToOwner.reviewTargetId.eq(memberId)
                        .and(reviewToOwner.delYn.eq(delYn)))
                .leftJoin(reviewToWalker)
                .on(reviewToWalker.reviewTargetId.eq(memberId)
                        .and(reviewToWalker.delYn.eq(delYn)))
                .join(address)
                .on(address.memberId.eq(memberId)
                        .and(address.basicAddressYn.eq("Y"))
                        .and(address.delYn.eq(delYn)))
                .where(member.memberId.eq(memberId)
                        .and(member.delYn.eq(delYn)))
                .groupBy(
                        member.nickname,
                        address.dongAddress,
                        member.introduce,
                        member.name,
                        member.gender,
                        member.birthDate,
                        member.phone,
                        member.profile,
                        member.dogOwnership,
                        member.dogWalkingExperienceYn,
                        member.availabilityWithSize
                )
                .fetchOne();
    }

    // 반복되는 numberTemplate 로직을 메서드로 추출 (별점 평균 구하기)
    private NumberExpression<Float> calculateAverage(QReviewToWalker reviewToWalker, Path<?> field) {
        return Expressions.numberTemplate(Float.class,
                "CASE WHEN count({0}) > 0 THEN sum({1}) / count({0}) ELSE 0 END",
                reviewToWalker, field);
    }
}
