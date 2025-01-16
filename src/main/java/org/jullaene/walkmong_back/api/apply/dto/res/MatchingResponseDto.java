package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MatchingResponseDto {
    private final Long boardId;
    private final String tabStatus;
    private final String dogName;
    private final Gender dogGender;
    private final String dogProfile;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String dongAddress;
    private final Double distance;
    private final String walkerName;
    private final String walkerProfile;
    private final String walkMatchingStatus;


    public MatchingResponseDto() {
        this.boardId = null;
        this.tabStatus = null;
        this.dogName = null;
        this.dogGender = null;
        this.dogProfile = null;
        this.startTime = null;
        this.endTime = null;
        this.dongAddress = null;
        this.distance = null;
        this.walkerName = null;
        this.walkerProfile = null;
        this.walkMatchingStatus = null;
    }
}
