package org.jullaene.walkmong_back.api.apply.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.QApply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.*;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepositoryCustom;
import org.jullaene.walkmong_back.api.board.domain.QBoard;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.api.review.domain.QHashtagToWalker;
import org.jullaene.walkmong_back.api.review.domain.QReviewToWalker;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.list;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@RequiredArgsConstructor
@Slf4j
public class ApplyRepositoryImpl implements ApplyRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QDog dog = QDog.dog;
    private final QMember member = QMember.member;
    private final QBoard board = QBoard.board;
    private final QApply apply = QApply.apply;
    private final QAddress address = QAddress.address;
    /**
     산책 지원시 지원서 내용 최종 확인하기
     */
    @Override
    public Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn) {
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

    /**
     내가 지원한 산책 내역 확인하기
     */
    @Override
    public List<AppliedInfoResponseDto> getApplyRecordResponse(Long memberId, MatchingStatus status,String delYn) {
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
                                .and(apply.matchingStatus.eq(status))
                                .and(board.delYn.eq(delYn)))
                        .fetch();
            return appliedInfoDto;
    }


    /**
     * 반려인이 산책 지원자들의 정보를 조회한다
     */
    public List<ApplicantListResponseDto> getApplicantList(Long boardId, Long memberId, String delYn){
        List<ApplicantListResponseDto> applicantList=
                queryFactory.selectDistinct(
                                Projections.constructor(ApplicantListResponseDto.class,
                                        member.nickname.as("applicantName"),
                                        member.profile.as("applicantProfile"),
                                        Expressions.numberTemplate(Integer.class,"YEAR(CURDATE()) - YEAR({0})", member.birthDate).as("applicantAge"),
                                        member.gender.as("applicantGender"),
                                        address.dongAddress.as("dongAddress"),
                                        address.roadAddress.as("roadAddress"),
                                        Expressions.asNumber(100) //평점
                                ))
                        .from(board)
                        .leftJoin(apply).on(apply.boardId.eq(board.boardId))
                        .leftJoin(member).on(apply.memberId.eq(member.memberId))
                        .leftJoin(address).on(apply.memberId.eq(address.memberId))
                        .where((apply.matchingStatus.eq(MatchingStatus.valueOf("PENDING")))
                                .and(board.delYn.eq(delYn)))
                        .fetch();

        return applicantList;
    }

    /**
     지원자의 정보와 지원한 산책 정보를 리턴한다
     */
    @Override
    public WalkerInfoResponseDto getApplicantInfo(Long boardId, Long walkerId) {
        WalkerInfoResponseDto walkerInfo=
                queryFactory.selectDistinct(
                                Projections.constructor(WalkerInfoResponseDto.class,
                                        member.nickname.as("name"),
                                        Expressions.numberTemplate(Integer.class,"YEAR(CURDATE()) - YEAR({0})", member.birthDate).as("age"),
                                        member.gender.as("gender"),
                                        member.profile.as("profile"),
                                        address.dongAddress.as("walkerDongAddress"),
                                        apply.dongAddress.as("dongAddress"),
                                        apply.roadAddress.as("roadAddress"),
                                        apply.addressDetail.as("addressDetail"),
                                        apply.addressMemo.as("addressMemo"),
                                        apply.poopBagYn.as("poopBagYn"),
                                        apply.muzzleYn.as("muzzleYn"),
                                        apply.dogCollarYn.as("dogCollarYn"),
                                        apply.preMeetingYn.as("preMettingYn"),
                                        apply.memoToOwner.as("memoToOwner")
                                ))
                        .from(apply)
                        .leftJoin(member).on(member.memberId.eq(apply.memberId))
                        .leftJoin(address).on(address.memberId.eq(member.memberId))
                        .where(apply.memberId.eq(walkerId))
                        .fetchOne();

        return walkerInfo;
    }



}