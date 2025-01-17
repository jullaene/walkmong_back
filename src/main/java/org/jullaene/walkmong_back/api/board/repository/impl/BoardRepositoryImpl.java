package org.jullaene.walkmong_back.api.board.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.QApply;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.res.MatchingResponseDto;
import org.jullaene.walkmong_back.api.board.domain.QBoard;
import org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus;
import org.jullaene.walkmong_back.api.board.dto.res.BoardDetailResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardResponseDto;
import org.jullaene.walkmong_back.api.board.repository.BoardRepositoryCustom;
import org.jullaene.walkmong_back.api.chat.domain.QChat;
import org.jullaene.walkmong_back.api.chat.domain.QChatRoom;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.dog.domain.QDog;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.common.enums.TabStatus;
import org.jullaene.walkmong_back.common.exception.ErrorType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.min;

@Slf4j
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<BoardResponseDto> getBoardsWithFilters(LocalDate date, Address walkerAddress, DistanceRange distance, DogSize dogSize, String matchingYn) {
       QBoard board = QBoard.board;
       QDog dog = QDog.dog;
       QAddress ownerAddress = QAddress.address;

        BooleanBuilder builder = new BooleanBuilder();

        // date가 null이면 당일을 조건에 삽입
        if (date == null) {
            date = LocalDate.now();
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999_999_999);

        builder.and(board.startTime.between(startOfDay, endOfDay));

        if (dogSize != null && !matchingYn.isBlank()) {
            builder.and(dog.dogSize.eq(dogSize));
        }

        if (matchingYn != null && !matchingYn.isBlank()) {
            builder.and(board.matchingYn.eq(matchingYn));
        }

        if (distance == null) {
            distance = walkerAddress.getDistanceRange();
        }

        // 거리 계산
        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                JPAExpressions.select(ownerAddress.longitude)
                        .from(ownerAddress)
                        .where(ownerAddress.addressId.eq(board.ownerAddressId)
                                .and(ownerAddress.delYn.eq("N"))),
                JPAExpressions.select(ownerAddress.latitude)
                        .from(ownerAddress)
                        .where(ownerAddress.addressId.eq(board.ownerAddressId)
                                .and(ownerAddress.delYn.eq("N"))),
                walkerAddress.getLongitude(),
                walkerAddress.getLatitude()
        );

        BooleanExpression isWithinRange = distanceExpression.lt(distance.getRange());

        StringTemplate startTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.startTime
        );
        StringTemplate endTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.endTime
        );

        // 필터링된 board들을 바로 결과로 조회
        return queryFactory.select(
                        Projections.constructor(BoardResponseDto.class,
                                board.boardId.as("boardId"),
                                startTimeExpression.as("startTime"),
                                endTimeExpression.as("endTime"),
                                board.matchingYn.as("matchingYn"),
                                dog.name.as("dogName"),
                                dog.profile.as("dogProfile"),
                                dog.gender.as("dogGender"),
                                dog.breed.as("breed"),
                                dog.weight.as("weight"),
                                dog.dogSize.as("dogSize"),
                                board.content.as("content"),
                                ownerAddress.dongAddress.as("dongAddress"),
                                distanceExpression.as("distance"),
                                board.createdAt
                        )
                )
                .from(board)
                .join(dog).on(dog.dogId.eq(board.dogId).and(dog.delYn.eq("N")))
                .join(ownerAddress).on(ownerAddress.addressId.eq(board.ownerAddressId).and(ownerAddress.delYn.eq("N")))
                .where(builder
                        .and(board.delYn.eq("N")))
                .where(isWithinRange)
                .orderBy(board.startTime.asc())
                .fetch();
    }

    /**
     * boardId와 memberId를 기준으로 해당 게시글의 작성자인지 확인
     * */
    @Override
    public boolean existsByBoardIdAndMemberIdAndDelYn(Long boardId, Long memberId, String delYn) {
        QBoard board = QBoard.board;
        QDog dog = QDog.dog;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.boardId.eq(boardId));
        builder.and(board.delYn.eq(delYn));
        builder.and(dog.memberId.eq(memberId));

        long count = queryFactory
                .selectFrom(board)
                .join(dog).on(board.dogId.eq(dog.dogId).and(dog.delYn.eq(delYn)))
                .where(builder)
                .fetch()
                .size();

        return count > 0;
    }

    @Override
    public Optional<BoardDetailResponseDto> getBoardDetailResponse(Long boardId, Long memberId, String delYn) {
        QDog dog = QDog.dog;
        QAddress address = QAddress.address;
        QBoard board = QBoard.board;
        QMember member = QMember.member;
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

        // 산책자의 기본 주소 위도, 경도 추출
        Tuple result = queryFactory
                .select(address.latitude, address.longitude)
                .from(address)
                .where(
                        address.memberId.eq(memberId)
                                .and(address.basicAddressYn.eq("Y"))
                )
                .fetchOne();
        Objects.requireNonNull(result, ErrorType.INVALID_ADDRESS.getMessage());

        Double walkerLatitude = result.get(address.latitude);
        Double walkerLongitude = result.get(address.longitude);

        // 거리 계산
        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                JPAExpressions.select(address.longitude)
                        .from(address)
                        .where(address.addressId.eq(board.ownerAddressId)),
                JPAExpressions.select(address.latitude)
                        .from(address)
                        .where(address.addressId.eq(board.ownerAddressId)),
                walkerLongitude,
                walkerLatitude
        );

        double distance = 500;

        return
                Optional.ofNullable(queryFactory.select(
                                Projections.constructor(BoardDetailResponseDto.class,
                                        dog.dogId.as("dogId"),
                                        dog.name.as("dogName"),
                                        dog.profile.as("dogProfile"),
                                        dog.gender.as("dogGender"),
                                        Expressions.numberOperation(Integer.class, Ops.SUB,
                                                Expressions.constant(currentYear), dog.birthYear).as("dogAge"),
                                        dog.breed.as("breed"),
                                        dog.weight.as("weight"),
                                        dog.dogSize.as("dogSize"),
                                        address.dongAddress.as("dongAddress"),
                                        distanceExpression.as("distance"),
                                        dateExpression.as("date"),
                                        startTimeExpression.as("startTime"),
                                        endTimeExpression.as("endTime"),
                                        board.locationNegotiationYn.as("locationNegotiationYn"),
                                        board.preMeetAvailableYn.as("preMeetAvailableYn"),
                                        dog.walkNote.as("walkNote"),
                                        dog.walkRequest.as("walkRequest"),
                                        dog.additionalRequest.as("additionalRequest"),
                                        member.name.as("ownerName"),
                                        Expressions.numberOperation(Integer.class, Ops.SUB,
                                                Expressions.constant(currentYear),
                                                birthYearExpression).as("ownerAge"),
                                        member.gender.as("ownerGender"),
                                        member.profile.as("ownerProfile"),
                                        board.createdAt

                                ))
                        .from(board)
                        .join(dog).on(dog.dogId.eq(board.dogId)
                                .and(dog.delYn.eq(delYn)))
                        .join(member).on(board.ownerId.eq(member.memberId)
                                .and(member.delYn.eq(delYn)))
                        .join(address).on(address.addressId.eq(board.ownerAddressId)
                                .and(address.delYn.eq(delYn)))
                        .where(board.boardId.eq(boardId)
                                .and(board.delYn.eq(delYn)))
                        .fetchOne());
    }

    /**
     * 의뢰한 산책의 채팅방 리스트 조회하기
     * */
    @Override
    public List<ChatRoomListResponseDto> getRequestChatList(Long memberId, MatchingStatus status) {
        QChatRoom chatRoom=QChatRoom.chatRoom;
        QChat chat= QChat.chat;
        QMember member=QMember.member;
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
                                        chatRoom.chatParticipantId.as("chatTarget"), //채팅 대상
                                        chat.message.as("lastChat"), //상대의 마지막 채팅
                                        chat.createdAt.as("lastChatTime"),  // 상대의 마지막 채팅 내용
                                        Expressions.asString("").as("targetName"), // 상대방 이름을 공백으로 지정
                                        Expressions.asNumber(10).as("notRead"),
                                        chat.roomId.as("roomId"),
                                        board.boardId.as("boardId")
                                ))
                        .from(board)
                        .join(dog).on(dog.dogId.eq(board.dogId)
                                .and(dog.delYn.eq("N")))
                        .join(apply).on(apply.boardId.eq(board.boardId)
                                .and(apply.delYn.eq(apply.delYn)))
                        //boardId와 chatParticipantId는 1:1 관계
                        .join(chatRoom).on(chatRoom.boardId.eq(board.boardId)
                                .and(chatRoom.delYn.eq("N")))
                        .leftJoin(chat).on(chat.roomId.eq(chatRoom.roomId)
                                .and(chat.delYn.eq("N")))
                        .join(member).on(apply.memberId.eq(member.memberId)
                                .and(member.delYn.eq("N"))) //산책 지원자
                        .where(board.ownerId.eq(memberId)
                                .and(apply.matchingStatus.eq(status))
                                .and(chat.chatId.eq(lastMessageSubQuery)))
                        .fetch();

        return chatRoomListResponseDtos;
    }


    /*
     * 매칭 현황 조회 시 게시글 미리보기
     */
    @Override
    public BoardPreviewResponseDto getBoardPreview(Long boardId,Long memberId,String delYn){
        QBoard board=QBoard.board;
        QDog dog=QDog.dog;
        QAddress address=QAddress.address;

        StringTemplate startTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.startTime
        );
        StringTemplate endTimeExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, '%H:%i')",
                board.endTime
        );

        BoardPreviewResponseDto previewDto =
                queryFactory.selectDistinct(
                                Projections.constructor(BoardPreviewResponseDto.class,
                                        dog.name.as("dogName"),
                                        dog.profile.as("dogProfile"),
                                        dog.gender.as("dogGender"),
                                        address.dongAddress.as("dongAddress"),
                                        board.content.as("content"),
                                        startTimeExpression.as("startTime"),
                                        endTimeExpression.as("endTime")
                                ))
                        .from(board)
                        .join(dog).on(dog.dogId.eq(board.dogId)
                                .and(dog.delYn.eq(dog.delYn)))
                        .join(address).on(address.addressId.eq(board.ownerAddressId)
                                .and(address.delYn.eq(address.delYn)))
                        .where(board.boardId.eq(boardId)
                                .and(board.delYn.eq(delYn)))
                        .fetchOne();
        return previewDto;
    }

    @Override
    public List<MatchingResponseDto> getBoardInfoResponse(Long memberId, WalkMatchingStatus status, String delYn) {
        QBoard board = QBoard.board;
        QDog dog = QDog.dog;
        QMember member = QMember.member;
        QApply apply = QApply.apply;

        BooleanBuilder builder = new BooleanBuilder();
        LocalDateTime now = LocalDateTime.now();

        log.info("memberId : " + memberId);

        // 매칭 전 : 지원 상태가 PENDING이고 산책 날짜 안 지남
        if (status.equals(WalkMatchingStatus.PENDING)) {
            builder.and(board.walkingStatus.eq(WalkingStatus.PENDING))
                    .and(board.startTime.after(now));
        }
        // 매칭 확정 : 지원 상태가 CONFIRMED이고 날짜 안 지남
        else if (status.equals(WalkMatchingStatus.BEFORE)) {
            builder.and(board.walkingStatus.eq(WalkingStatus.BEFORE))
                    .and(board.startTime.after(now));
        }
        // 산책 완료 : 지원 상태가 CONFIRMED이고 날짜 지남
        else if (status.equals(WalkMatchingStatus.AFTER)) {
            builder.and(board.walkingStatus.eq(WalkingStatus.AFTER));
        }
        // 매칭 취소 : 지원 상태가 REJECT이거나 지원 상태가 PENDING인데 날짜 지남
        else if (status.equals(WalkMatchingStatus.REJECT)) {
            log.info("매칭 취소 조건 추가");
            builder.and(board.walkingStatus.eq(WalkingStatus.PENDING))
                    .and(board.startTime.before(now));
        }

        return queryFactory.selectDistinct(
                        Projections.constructor(MatchingResponseDto.class,
                                Expressions.constant(TabStatus.BOARD.name()),
                                dog.name,                    // String
                                dog.gender,                  // Gender (Enum type 그대로 전달)
                                dog.profile,                 // String
                                board.startTime,             // LocalDateTime
                                board.endTime,               // LocalDateTime
                                Expressions.nullExpression(String.class),
                                Expressions.nullExpression(Double.class),
                                member.name,                 // String
                                member.profile,              // String
                                Expressions.asString(status.name()),  // String
                                board.boardId,               // Long
                                board.content,               // String
                                Expressions.nullExpression(Long.class),
                                count(apply)                 // Long
                        ))
                .from(board)
                .join(dog)
                .on(dog.dogId.eq(board.dogId)
                        .and(dog.delYn.eq(delYn)))
                .join(apply)
                .on(apply.boardId.eq(board.boardId)
                        .and(apply.delYn.eq(delYn))
                        .and(board.walkingStatus.eq(WalkingStatus.PENDING))
                )
                .leftJoin(member)
                .on(member.memberId.eq(apply.memberId)
                        .and(member.delYn.eq(delYn))
                        .and(board.walkingStatus.ne(WalkingStatus.PENDING))
                        .and(apply.matchingStatus.eq(MatchingStatus.CONFIRMED)))
                .where(board.ownerId.eq(memberId)
                        .and(board.delYn.eq(delYn))
                )
                .where(builder)
                .groupBy(
                        dog.name,
                        dog.gender,
                        dog.profile,
                        board.startTime,
                        board.endTime,
                        member.name,
                        member.profile,
                        board.boardId,
                        board.content,
                        Expressions.asString(status.name())
                )
                .fetch();
    }

}
