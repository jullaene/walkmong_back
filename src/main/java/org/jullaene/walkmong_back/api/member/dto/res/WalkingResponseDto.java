package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.member.dto.common.WalkingBasicInfo;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagPercentageDto;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
public class WalkingResponseDto {
    private final String nickname;
    private final String dongAddress;
    private final String introduction;
    private final String name;
    private final Gender gender;
    private final LocalDate birthDate;
    private final String phone;
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
        this.nickname = walkingBasicInfo.getNickname();
        this.dongAddress = walkingBasicInfo.getDongAddress();
        this.introduction = walkingBasicInfo.getIntroduction();
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
        this.gender = walkingBasicInfo.getGender();
        this.birthDate = walkingBasicInfo.getBirthDate();
        this.phone = walkingBasicInfo.getPhone();
        this.tags = topHashtags;
    }
}
