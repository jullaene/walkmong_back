package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WalkingDtlRes {
    private final String date;
    private final String startTime;
    private final String endTime;
    private final Long dogId;
    private final String dogName;
    private final Gender dogGender;
    private final String content;
    private final String ownerDongAddress;
    private final Long walkerId;
    private final String walkerNickname;
    private final Gender walkerGender;
    private final Integer walkerAge;
    private final String walkRequest;
    private final String walkNote;
    private final String additionalRequest;
    private final Double latitude;
    private final Double longitude;
    private final String roadAddress;
    private final String addressDetail;
    private final String addressMemo;
    private final String memoToOwner;

}
