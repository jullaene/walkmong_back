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


    public final DogProfileResponseDto toDogProfileResponseDto() {
        int currentYear = LocalDate.now().getYear();
        int dogAge = currentYear - this.birthYear + 1; // 나이 계산

        return DogProfileResponseDto.builder()
                .dogId(this.dogId)
                .dogName(this.name)
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
                .build();
    }

    @Builder
    public Dog(Long memberId,String name,
               DogSize dogSize, String profile,
               Gender gender, Integer birthYear,
               String breed, Double weight,
               String neuteringYn, String bite,
               String friendly, String barking,
               String rabiesYn, String adultYn){
        this.name=name;
        this.memberId=memberId;
        this.dogSize=dogSize;
        this.profile=profile;
        this.gender=gender;
        this.birthYear=birthYear;
        this.breed=breed;
        this.weight=weight;
        this.neuteringYn=neuteringYn;
        this.bite = bite;
        this.friendly=friendly;
        this.barking = barking;
        this.rabiesYn=rabiesYn;
        this.adultYn=adultYn;
    }
}
