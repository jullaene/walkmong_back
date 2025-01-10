package org.jullaene.walkmong_back.api.dog.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class DogProfileReqDto {
    private final Long memberId;
    private final String name;
    private final DogSize dogSize;
    private final MultipartFile profile;
    private final Gender gender;
    private final Integer birthYear;
    private final String breed;
    private final Double weight;
    private final String neuteringYn;
    private final String bite;
    private final String friendly;
    private final String barking;
    private final String rabiesYn;
    private final String adultYn;
    private final String walkRequest;
    private final String walkNote;
    private final String additionalRequest;
}
