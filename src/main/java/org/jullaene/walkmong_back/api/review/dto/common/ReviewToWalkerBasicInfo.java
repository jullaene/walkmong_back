package org.jullaene.walkmong_back.api.review.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewToWalkerBasicInfo {
    private final Long reviewToWalkerId;
    private final String ownerName;
    private final String dogName;
    private final LocalDateTime walkingDay;
    private final Float photoSharing;
    private final Float attitude;
    private final Float taskCompletion;
    private final Float timePunctuality;
    private final Float communication;
    private final String content;
}
