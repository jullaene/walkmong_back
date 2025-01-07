package org.jullaene.walkmong_back.api.member.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalkingBasicInfo {
    private final String name;
    private final String profile;
    private final String dogOwnership;
    private final Long dogWalkingExperience;
    private final String availabilityWithSize;
    private final Long walkerReviewCount;
    private final Float photoSharing;
    private final Float attitude;
    private final Float taskCompletion;
    private final Float timePunctuality;
    private final Float communication;
    private final Long ownerReviewCount;
    private final Integer goodPercent;
}
