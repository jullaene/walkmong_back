package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.member.dto.common.WalkingBasicInfo;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagPercentageDto;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;

import java.util.List;

@Getter
public class WalkingResponseDto {
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
    private final List<HashtagPercentageDto> tags;
    private final Long ownerReviewCount;
    private final Integer goodPercent;

    @Builder
    public WalkingResponseDto(
            WalkingBasicInfo walkingBasicInfo,
            List<HashtagPercentageDto> topHashtags
    ) {
        this.name = walkingBasicInfo.getName();
        this.profile = walkingBasicInfo.getProfile();
        this.dogOwnership = walkingBasicInfo.getDogOwnership();
        this.dogWalkingExperience = walkingBasicInfo.getDogWalkingExperience();
        this.availabilityWithSize = walkingBasicInfo.getAvailabilityWithSize();
        this.walkerReviewCount = walkingBasicInfo.getWalkerReviewCount();
        this.photoSharing = walkingBasicInfo.getPhotoSharing();
        this.attitude = walkingBasicInfo.getAttitude();
        this.taskCompletion = walkingBasicInfo.getTaskCompletion();
        this.timePunctuality = walkingBasicInfo.getTimePunctuality();
        this.communication = walkingBasicInfo.getCommunication();
        this.ownerReviewCount = walkingBasicInfo.getOwnerReviewCount();
        this.goodPercent = walkingBasicInfo.getGoodPercent();
        this.tags = topHashtags;
    }
}
