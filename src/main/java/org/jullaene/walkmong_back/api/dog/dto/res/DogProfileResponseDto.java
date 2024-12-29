package org.jullaene.walkmong_back.api.dog.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

@Getter
@Builder
public class DogProfileResponseDto {
    private Long dogId;
    private String dogName;
    private DogSize dogSize;
    private String dogProfile;
    private Gender dogGender;
    private Integer dogAge;
    private String breed;
    private Double weight;
    private String neuteringYn;
    private String bite;
    private String friendly;
    private String barking;
    private String rabiesYn;
    private String adultYn;
    private String walkRequest;
    private String walkNote;
    private String additionalRequest;
}
