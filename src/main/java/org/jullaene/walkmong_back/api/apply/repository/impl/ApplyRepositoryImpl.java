package org.jullaene.walkmong_back.api.apply.repository.impl;

import com.querydsl.core.types.Projections;
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
import org.jullaene.walkmong_back.api.member.domain.QMember;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ApplyRepositoryImpl implements ApplyRepositoryCustom{
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

        List<AppliedInfoResponseDto> appliedInfoDto=
                queryFactory.selectDistinct(
                                Projections.constructor(AppliedInfoResponseDto.class,
                                        dog.name.as("dogName"),
                                        dog.gender.as("dogGender"),
                                        dog.profile.as("dogProfile"),
                                        apply.dongAddress.as("dongAddress"),
                                        apply.addressDetail.as("addressDetail"),
                                        board.startTime.as("startTime"),
                                        board.endTime.as("endTime")
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
