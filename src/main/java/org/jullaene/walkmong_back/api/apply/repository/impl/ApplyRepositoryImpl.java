package org.jullaene.walkmong_back.api.apply.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.QApply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.AppliedInfoResponseDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepositoryCustom;
import org.jullaene.walkmong_back.api.board.domain.QBoard;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@RequiredArgsConstructor
@Slf4j
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //산책 지원 최종 내역 확인하기
    @Override
    public Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn) {
        QDog dog= QDog.dog;
        QMember member=QMember.member;
        QBoard board=QBoard.board;
        QApply apply= QApply.apply;

        Optional<ApplyInfoDto> applyInfoDto=
                Optional.ofNullable(queryFactory.selectDistinct(
                                Projections.constructor(ApplyInfoDto.class,
                                        dog.name.as("dogName"),
                                        dog.gender.as("dogGender"),
                                        dog.breed.as("breed"),
                                        dog.dogSize.as("dogSize"),
                                        member.name.as("ownerName"),
                                        member.profile.as("memberProfile"),
                                        member.gender.as("memberGender"),
                                        apply.dongAddress.as("dongAddress"),
                                        apply.addressDetail.as("addressDetail"),
                                        apply.muzzleYn.as("muzzleYn"),
                                        apply.poopBagYn.as("poopBagYn"),
                                        apply.preMeetingYn.as("preMeetingYn"),
                                        apply.memoToOwner.as("memoToOwner"),
                                        board.startTime.as("startTime"),
                                        board.endTime.as("endTime")
                                ))
                        .from(board)
                        .leftJoin(dog).on(dog.dogId.eq(board.dogId))
                        .leftJoin(member).on(dog.memberId.eq(member.memberId))
                        .leftJoin(apply).on(apply.boardId.eq(boardId))
                        .where(board.boardId.eq(boardId)
                                .and(board.delYn.eq(delYn)))
                        .fetchOne());

        return applyInfoDto;
    }

    //지원한 산책 내역 확인하기
    @Override
    public List<AppliedInfoResponseDto> getApplyRecordResponse(Long memberId, MatchingStatus status) {
        QDog dog= QDog.dog;
        QMember member=QMember.member;
        QBoard board=QBoard.board;
        QApply apply= QApply.apply;
        QAddress address=QAddress.address;

        // 거리 계산
        NumberTemplate<Double> distanceExpression = numberTemplate(
                Double.class,
                "(CAST(ST_Distance_Sphere(point({0}, {1}), point({2}, {3})) AS DOUBLE) / 1000)",
                //산책 지원자의 위도,경도
                JPAExpressions.select(address.longitude)
                        .from(address)
                        .where(address.memberId.eq(memberId)),
                JPAExpressions.select(address.latitude)
                        .from(address)
                        .where(address.memberId.eq(memberId)),
                //산책 요청자의 위도, 경도
                JPAExpressions.select(address.longitude)
                        .from(address)
                        .where(address.memberId.eq(
                                JPAExpressions.select(board.ownerId)
                                        .from(board)
                                        .where(board.boardId.eq(apply.boardId))
                        )),
                JPAExpressions.select(address.latitude)
                        .from(address)
                        .where(address.memberId.eq(
                                JPAExpressions.select(board.ownerId)
                                        .from(board)
                                        .where(board.boardId.eq(apply.boardId))
                        ))
        );

        List<AppliedInfoResponseDto> appliedInfoDto=
                queryFactory.selectDistinct(
                                Projections.constructor(AppliedInfoResponseDto.class,
                                        dog.name.as("dogName"),
                                        dog.gender.as("dogGender"),
                                        dog.profile.as("dogProfile"),
                                        apply.dongAddress.as("dongAddress"),
                                        apply.addressDetail.as("addressDetail"),
                                        board.startTime.as("startTime"),
                                        board.endTime.as("endTime"),
                                        distanceExpression.as("distance")
                                ))
                        .from(board)
                        .leftJoin(dog).on(dog.dogId.eq(board.dogId))
                        .leftJoin(apply).on(apply.boardId.eq(board.boardId))
                        .where(apply.memberId.eq(memberId)
                                .and(apply.matchingStatus.eq(status)))
                        .fetch();
            return appliedInfoDto;
    }
}
