package org.jullaene.walkmong_back.api.apply.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
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
import org.jullaene.walkmong_back.api.chat.domain.QChat;
import org.jullaene.walkmong_back.api.chat.domain.QChatRoom;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.list;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@RequiredArgsConstructor
@Slf4j
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {
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

    //지원한 산책의 채팅리스트 조회
    @Override
    public List<ChatRoomListResponseDto> getApplyChatList(Long memberId, MatchingStatus status) {
        QChatRoom chatRoom=QChatRoom.chatRoom;
        QChat chat= QChat.chat;
        QDog dog= QDog.dog;
        QBoard board=QBoard.board;
        QApply apply= QApply.apply;

        //대화 상대의 마지막 메세지 가져오기: chat 테이블에서 Id의 최댓값을 가져온다
        SubQueryExpression<Long> lastMessageSubQuery = JPAExpressions.select(chat.chatId.max())
                .from(chat)
                .leftJoin(chatRoom).on(chat.roomId.eq(chatRoom.roomId))
                .where(chat.roomId.eq(chatRoom.roomId)
                        .and(chat.senderId.ne(memberId)));



        List<ChatRoomListResponseDto> chatRoomListResponseDtos=
                queryFactory.selectDistinct(
                                Projections.constructor(ChatRoomListResponseDto.class,
                                        dog.name.as("dogName"),
                                        dog.profile.as("dogProfile"),
                                        board.startTime.as("startTime"),
                                        board.endTime.as("endTime"),
                                        chatRoom.chatOwnerId.as("chatTarget"), //채팅 대상
                                        chat.message.as("lastChat"), //상대의 마지막 채팅
                                        chat.createdAt.as("lastChatTime"),  // 상대의 마지막 채팅 내용
                                        Expressions.asString("").as("targetName"),// 상대방 이름을 공백으로 지정
                                        Expressions.asNumber(10).as("notRead"),
                                        chat.roomId.as("roomId")

                                ))
                        .from(board)
                        .leftJoin(dog).on(dog.dogId.eq(board.dogId))
                        .leftJoin(apply).on(apply.boardId.eq(board.boardId))
                        //boardId와 chatParticipantId는 1:1 관계
                        .leftJoin(chatRoom).on(chatRoom.boardId.eq(board.boardId))
                        .leftJoin(chat).on(chat.roomId.eq(chatRoom.roomId))
                        .where(apply.memberId.eq(memberId)
                                .and(apply.matchingStatus.eq(status))
                                .and(chat.chatId.eq(lastMessageSubQuery)))
                        .fetch();

        return chatRoomListResponseDtos;
    }

    /**
     * 반려인이 산책 지원자들의 정보를 조회한다
     */
    public List<ApplicantInfoResponseDto> getApplicantList(Long boardId,String delYn){
        List<ApplicantInfoResponseDto> applicantList=
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
                        .leftJoin(apply).on(apply.boardId.eq(board.boardId))
                        .leftJoin(member).on(apply.memberId.eq(member.memberId))
                        .leftJoin(address).on(apply.memberId.eq(address.memberId))
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
        ApplicantInfoResponseDto applicant=
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
                        .leftJoin(apply).on(apply.boardId.eq(board.boardId))
                        .leftJoin(member).on(apply.memberId.eq(member.memberId))
                        .leftJoin(address).on(apply.memberId.eq(address.memberId))
                        .where((apply.matchingStatus.eq(MatchingStatus.valueOf("PENDING")))
                                .and(board.delYn.eq(delYn))
                                .and(apply.applyId.eq(applyId))
                                .and(board.boardId.eq(boardId)))
                        .fetchOne();

        return applicant;
    }

}
