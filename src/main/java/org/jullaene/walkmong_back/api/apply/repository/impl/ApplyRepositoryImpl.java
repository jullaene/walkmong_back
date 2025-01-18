package org.jullaene.walkmong_back.api.apply.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.QApply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.*;
import org.jullaene.walkmong_back.api.apply.repository.ApplyRepositoryCustom;
import org.jullaene.walkmong_back.api.board.domain.QBoard;
import org.jullaene.walkmong_back.api.chat.domain.QChat;
import org.jullaene.walkmong_back.api.chat.domain.QChatRoom;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.common.enums.TabStatus;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@RequiredArgsConstructor
@Slf4j
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    /**
     산책 지원시 지원서 내용 최종 확인하기
     */
    @Override
    public Optional<ApplyInfoDto> getApplyInfoResponse(Long boardId, Long memberId, String delYn) {
        final QDog dog = QDog.dog;
        final QMember member = QMember.member;
        final QBoard board = QBoard.board;
        final QApply apply = QApply.apply;

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
    public List<MatchingResponseDto> getApplyInfoResponses(Long memberId, WalkMatchingStatus status, String delYn) {
        QDog dog = QDog.dog;
        QBoard board = QBoard.board;
        QApply apply = QApply.apply;
        QAddress address = QAddress.address;

        BooleanBuilder builder = new BooleanBuilder();
        LocalDateTime now = LocalDateTime.now();

        // 매칭 전 : 지원 상태가 PENDING이고 산책 날짜 안 지남
        if (status.equals(WalkMatchingStatus.PENDING)) {
            builder.and(apply.matchingStatus.eq(MatchingStatus.PENDING))
                    .and(board.startTime.after(now));
        }
        // 매칭 확정 : 지원 상태가 CONFIRMED이고 날짜 안 지남
        else if (status.equals(WalkMatchingStatus.BEFORE)) {
            builder.and(apply.matchingStatus.eq(MatchingStatus.CONFIRMED))
                    .and(board.startTime.after(now));
        }
        // 산책 완료 : 지원 상태가 CONFIRMED이고 날짜 지남
        else if (status.equals(WalkMatchingStatus.AFTER)) {
            builder.and(apply.matchingStatus.eq(MatchingStatus.CONFIRMED))
                    .and(board.startTime.before(now));
        }
        // 매칭 취소 : 지원 상태가 REJECT이거나 지원 상태가 PENDING인데 날짜 지남
        else if (status.equals(WalkMatchingStatus.REJECT)) {
            builder.and(apply.matchingStatus.eq(MatchingStatus.REJECTED))
                    .or(apply.matchingStatus.eq(MatchingStatus.PENDING)
                            .and(board.startTime.before(now))
                    );
        }

        // 거리 계산
        NumberTemplate<Double> distanceExpression = numberTemplate(
                Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                //산책 지원자의 위도,경도
                JPAExpressions.select(apply.longitude),
                JPAExpressions.select(apply.latitude),
                //산책 요청자의 위도, 경도
                JPAExpressions.select(address.longitude)
                        .from(address)
                        .where(address.addressId.eq(board.ownerAddressId)
                                .and(address.delYn.eq(delYn))),
                JPAExpressions.select(address.latitude)
                        .from(address)
                        .where(address.addressId.eq(board.ownerAddressId)
                                .and(address.delYn.eq(delYn)))
        );

        return queryFactory.selectDistinct(
                                Projections.constructor(MatchingResponseDto.class,
                                        Expressions.constant(TabStatus.APPLY.name()),
                                        dog.name.as("dogName"),
                                        dog.gender.as("dogGender"),
                                        dog.profile.as("dogProfile"),
                                        board.startTime.as("startTime"),
                                        board.endTime.as("endTime"),
                                        apply.dongAddress.as("dongAddress"),
                                        distanceExpression.as("distance"),
                                        Expressions.nullExpression(String.class),
                                        Expressions.nullExpression(String.class),
                                        Expressions.asString(status.name()).as("walkMatchingStatus"),
                                        board.boardId.as("boardId"),
                                        board.content.as("content"),
                                        apply.applyId.as("applyId"),
                                        Expressions.nullExpression(Long.class)
                                ))
                .from(apply)
                .join(board)
                .on(board.boardId.eq(apply.boardId)
                        .and(board.delYn.eq(delYn))
                )
                .join(dog)
                .on(dog.dogId.eq(board.dogId)
                        .and(dog.delYn.eq(delYn)))
                .where(apply.memberId.eq(memberId)
                        .and(apply.delYn.eq(delYn))
                        .and(builder)
                )
                .fetch();
    }

    /**
     * 반려인이 산책 지원자들의 정보를 조회한다
     */
    public List<ApplicantInfoResponseDto> getApplicantList(Long boardId,String delYn){
        final QMember member = QMember.member;
        final QBoard board = QBoard.board;
        final QApply apply = QApply.apply;
        final QAddress address = QAddress.address;

        List<ApplicantInfoResponseDto> applicantList=
                queryFactory.selectDistinct(
                                Projections.constructor(ApplicantInfoResponseDto.class,
                                        member.nickname.as("applicantName"),
                                        member.profile.as("applicantProfile"),
                                        Expressions.numberTemplate(Integer.class,"YEAR(CURDATE()) - YEAR({0})", member.birthDate).as("applicantAge"),
                                        member.gender.as("applicantGender"),
                                        apply.dongAddress.as("applicantDongAddress"),
                                        apply.roadAddress.as("applicantRoadAddress"),
                                        Expressions.asNumber(100) //평점
                                ))
                        .from(board)
                        .join(apply).on(apply.boardId.eq(board.boardId)
                                .and(apply.delYn.eq(delYn)))
                        .join(member).on(apply.memberId.eq(member.memberId)
                                .and(member.delYn.eq(delYn)))
                        .where((apply.matchingStatus.eq(MatchingStatus.valueOf("PENDING")))
                                .and(board.delYn.eq(delYn))
                                .and(board.boardId.eq(boardId)))
                        .fetch();

        return applicantList;
    }

    /**
     * 반려인이 특정 산책자 정보를 조회한다
     */
    public ApplicantInfoResponseDto getApplicant(Long boardId,Long applyId, String delYn){
        final QMember member = QMember.member;
        final QBoard board = QBoard.board;
        final QApply apply = QApply.apply;
        final QAddress address = QAddress.address;

        ApplicantInfoResponseDto applicant =
                queryFactory.selectDistinct(
                                Projections.constructor(ApplicantInfoResponseDto.class,
                                        member.nickname.as("applicantName"),
                                        member.profile.as("applicantProfile"),
                                        Expressions.numberTemplate(Integer.class,"YEAR(CURDATE()) - YEAR({0})", member.birthDate).as("applicantAge"),
                                        member.gender.as("applicantGender"),
                                        address.dongAddress.as("applicantDongAddress"),
                                        address.roadAddress.as("applicantRoadAddress"),
                                        Expressions.asNumber(100) //평점
                                ))
                        .from(board)
                        .join(apply).on(apply.applyId.eq(applyId)
                                .and(apply.delYn.eq(delYn)))
                        .join(member).on(member.memberId.eq(apply.memberId)
                                .and(member.delYn.eq(delYn)))
                        .join(address).on(address.memberId.eq(apply.memberId)
                                .and(address.basicAddressYn.eq("Y"))
                                .and(address.delYn.eq(delYn)))
                        .where(board.delYn.eq(delYn)
                                .and(board.boardId.eq(boardId)))
                        .fetchOne();

        return applicant;
    }

    @Override
    public WalkingDtlRes getWalkingDtlRes(Long boardId, String delYn) {
        final QDog dog = QDog.dog;
        final QMember member = QMember.member;
        final QBoard board = QBoard.board;
        final QApply apply = QApply.apply;
        final QAddress address = QAddress.address;

        int currentYear = LocalDate.now().getYear() + 1;


        StringTemplate startTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.startTime
        );
        StringTemplate endTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.endTime
        );

        StringTemplate dateExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%Y-%m-%d')",
                board.startTime
        );

        // 숫자 형식의 생년월일에서 연도 추출
        NumberTemplate<Integer> birthYearExpression = Expressions.numberTemplate(Integer.class,
                "YEAR({0})",
                member.birthDate
        );

        return queryFactory
                .select(
                        Projections.constructor(WalkingDtlRes.class,
                            dateExpression.as("date"),
                            startTimeExpression.as("startTime"),
                            endTimeExpression.as("endTime"),
                            dog.dogId,
                            dog.name,
                            dog.gender,
                            board.content,
                            address.dongAddress,
                            member.memberId,
                            member.nickname,
                            member.gender,
                            Expressions.numberOperation(Integer.class, Ops.SUB,
                                    Expressions.constant(currentYear),
                                    birthYearExpression).as("walkerAge"),
                            dog.walkRequest,
                            dog.walkNote,
                            dog.additionalRequest,
                            apply.latitude,
                            apply.longitude,
                            apply.roadAddress,
                            apply.addressDetail,
                            apply.addressMemo,
                            apply.memoToOwner
                        )
                )
                .from(board)
                .join(dog)
                .on(dog.dogId.eq(board.dogId)
                        .and(dog.delYn.eq(delYn)))
                .join(address)
                .on(address.addressId.eq(board.ownerAddressId)
                        .and(address.delYn.eq(delYn)))
                .join(apply)
                .on(apply.boardId.eq(board.boardId)
                        .and(apply.matchingStatus.eq(MatchingStatus.CONFIRMED))
                        .and(apply.delYn.eq(delYn)))
                .join(member)
                .on(member.memberId.eq(apply.memberId)
                        .and(member.delYn.eq(delYn)))
                .where(board.boardId.eq(boardId)
                        .and(board.delYn.eq(delYn)))
                .fetchOne();
    }

}
