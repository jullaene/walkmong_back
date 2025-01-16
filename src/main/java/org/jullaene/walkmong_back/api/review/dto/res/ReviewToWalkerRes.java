package org.jullaene.walkmong_back.api.review.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.review.domain.enums.HashtagWalkerNm;
import org.jullaene.walkmong_back.api.review.dto.common.ReviewToWalkerBasicInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewToWalkerRes {
    private final Long reviewToWalkerId;
    private final String ownerName;
    private final String ownerProfile;
    private final String dogName;
    private final LocalDateTime walkingDay;
    private final Float photoSharing;
    private final Float attitude;
    private final Float taskCompletion;
    private final Float timePunctuality;
    private final Float communication;
    private final List<String> profiles;
    private final List<HashtagWalkerNm> hashtags;
    private final String content;

    @Builder
    public ReviewToWalkerRes(
            ReviewToWalkerBasicInfo reviewToWalkerBasicInfo,
            List<String> profiles,
            List<HashtagWalkerNm> hashtags
    ) {
        this.reviewToWalkerId = reviewToWalkerBasicInfo.getReviewToWalkerId();
        this.ownerName = reviewToWalkerBasicInfo.getOwnerName();
        this.ownerProfile = reviewToWalkerBasicInfo.getOwnerProfile();
        this.dogName = reviewToWalkerBasicInfo.getDogName();
        this.walkingDay = reviewToWalkerBasicInfo.getWalkingDay();
        this.photoSharing = reviewToWalkerBasicInfo.getPhotoSharing();
        this.attitude = reviewToWalkerBasicInfo.getAttitude();
        this.taskCompletion = reviewToWalkerBasicInfo.getTaskCompletion();
        this.timePunctuality = reviewToWalkerBasicInfo.getTimePunctuality();
        this.communication = reviewToWalkerBasicInfo.getCommunication();
        this.profiles = profiles;
        this.hashtags = hashtags;
        this.content = reviewToWalkerBasicInfo.getContent();
    }
}
