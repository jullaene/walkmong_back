package org.jullaene.walkmong_back.api.dog.dto.req;

import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

@Getter
public class DogProfileReqDto {
    private Long memberId;
    private String name;
    private DogSize dogSize;
    private String profile;
    private Gender gender;
    private Integer birthYear;
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
