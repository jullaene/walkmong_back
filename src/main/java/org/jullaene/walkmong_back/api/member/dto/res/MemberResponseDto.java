package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.api.review.dto.res.HashtagPercentageDto;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberResponseDto {
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
    private final List<DogProfileResponseDto> dogs;
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
    public MemberResponseDto(
            WalkingResponseDto walkingResponseDto,
            List<DogProfileResponseDto> dogs
    ) {
        this.nickname = walkingResponseDto.getNickname();
        this.dongAddress = walkingResponseDto.getDongAddress();
        this.introduction = walkingResponseDto.getIntroduction();
        this.name = walkingResponseDto.getName();
        this.gender = walkingResponseDto.getGender();
        this.birthDate = walkingResponseDto.getBirthDate();
        this.phone = walkingResponseDto.getPhone();
        this.profile = walkingResponseDto.getProfile();
        this.dogOwnership = walkingResponseDto.getDogOwnership();
        this.dogWalkingExperience = walkingResponseDto.getDogWalkingExperience();
        this.availabilityWithSize = walkingResponseDto.getAvailabilityWithSize();
        this.dogs = dogs;
        this.walkerReviewCount = walkingResponseDto.getWalkerReviewCount();
        this.photoSharing = walkingResponseDto.getPhotoSharing();
        this.attitude = walkingResponseDto.getAttitude();
        this.taskCompletion = walkingResponseDto.getTaskCompletion();
        this.timePunctuality = walkingResponseDto.getTimePunctuality();
        this.communication = walkingResponseDto.getCommunication();
        this.tags = walkingResponseDto.getTags();
        this.ownerReviewCount = walkingResponseDto.getOwnerReviewCount();
        this.goodPercent = walkingResponseDto.getGoodPercent();
    }
}
