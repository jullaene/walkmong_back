package org.jullaene.walkmong_back.api.dog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.dog.dto.req.DogProfileReqDto;
import org.jullaene.walkmong_back.api.dog.dto.res.DogProfileResponseDto;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "dog")
@Entity
@NoArgsConstructor
@DynamicUpdate
public class Dog extends BaseEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long dogId;

    @Comment("사용자 아이디")
    private Long memberId;

    @Comment("이름")
    private String name;

    @Comment("성별")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Comment("출생년도")
    private Integer birthYear;

    @Comment("무게")
    private Double weight;

    @Comment("품종")
    private String breed;

    @Comment("사이즈")
    @Enumerated(EnumType.STRING)
    private DogSize dogSize;

    @Comment("프로필 url")
    private String profile;

    @Comment("중성화 유무")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String neuteringYn;

    @Comment("입질 여부")
    private String bite;

    @Comment("친화력")
    private String friendly;

    @Comment("짖음 여부")
    private String barking;

    @Comment("성견 여부")
    private String adultYn;

    @Comment("광견병 접종 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String rabiesYn;

    @Comment("산책 참고 사항")
    private String walkRequest;

    @Comment("산책 요청 사항")
    private String walkNote;

    @Comment("추가 안내 사항")
    private String additionalRequest;

    @Builder
    public Dog(Long memberId, DogProfileReqDto dogProfileReqDto, String profileUrl){
        this.name = dogProfileReqDto.getName();
        this.memberId = memberId;
        this.dogSize = dogProfileReqDto.getDogSize();
        this.profile = profileUrl;
        this.gender = dogProfileReqDto.getGender();
        this.birthYear = dogProfileReqDto.getBirthYear();
        this.breed = dogProfileReqDto.getBreed();
        this.weight = dogProfileReqDto.getWeight();
        this.neuteringYn = dogProfileReqDto.getNeuteringYn();
        this.bite = dogProfileReqDto.getBite();
        this.friendly = dogProfileReqDto.getFriendly();
        this.barking = dogProfileReqDto.getBarking();
        this.rabiesYn = dogProfileReqDto.getRabiesYn();
        this.adultYn = dogProfileReqDto.getAdultYn();
        this.walkRequest = dogProfileReqDto.getWalkRequest();
        this.walkNote = dogProfileReqDto.getWalkNote();
        this.additionalRequest = dogProfileReqDto.getAdditionalRequest();
    }

    public final String getWalkRequestContent() {
        return this.walkRequest;
    }

    public final Long getMemberId () {
        return this.memberId;
    }

    public final DogProfileResponseDto toDogProfileResponseDto() {
        int currentYear = LocalDate.now().getYear();
        int dogAge = currentYear - this.birthYear + 1; // 나이 계산

        return DogProfileResponseDto.builder()
                .dogId(this.dogId)
                .dogName(this.name)
                .dogSize(this.dogSize)
                .dogProfile(this.profile)
                .dogGender(this.gender)
                .dogAge(dogAge)
                .breed(this.breed)
                .weight(this.weight)
                .neuteringYn(this.neuteringYn)
                .bite(this.bite)
                .friendly(this.friendly)
                .barking(this.barking)
                .rabiesYn(this.rabiesYn)
                .adultYn(this.adultYn)
                .walkRequest(this.walkRequest)
                .walkNote(this.walkNote)
                .additionalRequest(this.additionalRequest)
                .build();
    }

    public void updateProfile(DogProfileReqDto dogProfileDto, String profileUrl) {
        this.name = dogProfileDto.getName();
        this.profile = profileUrl;
        this.gender = dogProfileDto.getGender();
        this.birthYear = dogProfileDto.getBirthYear();
        this.weight = dogProfileDto.getWeight();
        this.breed = dogProfileDto.getBreed();
        this.dogSize = dogProfileDto.getDogSize();
        this.neuteringYn = dogProfileDto.getNeuteringYn();
        this.bite = dogProfileDto.getBite();
        this.friendly = dogProfileDto.getFriendly();
        this.barking = dogProfileDto.getBarking();
        this.rabiesYn = dogProfileDto.getRabiesYn();
        this.adultYn = dogProfileDto.getAdultYn();
        this.walkRequest = dogProfileDto.getWalkRequest();
        this.walkNote = dogProfileDto.getWalkNote();
        this.additionalRequest = dogProfileDto.getAdditionalRequest();
    }
}
