package org.jullaene.walkmong_back.api.board.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus;
import org.jullaene.walkmong_back.api.board.dto.req.BoardRequestDto;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "board")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class Board extends BaseEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Comment("반려동물 아이디")
    private Long dogId;

    @Comment("반려인 아이디")
    private Long ownerId;

    @Comment("반려인 주소 아이디")
    private Long ownerAddressId;

    @Comment("내용")
    private String content;

    @Comment("매칭 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String matchingYn;

    @Comment("산책 시작 시간")
    @Column(name = "start_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Comment("산책 종료 시간")
    @Column(name = "end_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @Comment("만남 장소 협의 여부")
    private String locationNegotiationYn;

    @Comment("사전 만남 가능 여부")
    private String preMeetAvailableYn;

    @Comment("산책 진행 여부")
    @Enumerated(EnumType.STRING)
    private WalkingStatus walkingStatus;

    @Builder
    public Board (BoardRequestDto boardRequestDto, String content, Long ownerId) {
        this.dogId = boardRequestDto.getDogId();
        this.ownerAddressId = boardRequestDto.getAddressId();
        this.content = content;
        this.matchingYn = "N";
        this.startTime = boardRequestDto.getStartTime();
        this.endTime = boardRequestDto.getEndTime();
        this.locationNegotiationYn = boardRequestDto.getLocationNegotiationYn();
        this.preMeetAvailableYn = boardRequestDto.getPreMeetAvailableYn();
        this.walkingStatus = WalkingStatus.BEFORE;
        this.ownerId = ownerId;
    }

}
