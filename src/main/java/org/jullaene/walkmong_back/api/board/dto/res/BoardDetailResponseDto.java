package org.jullaene.walkmong_back.api.board.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponseDto {

    private Long dogId;
    private String dogName;
    private String dogProfile;
    private Gender dogGender;
    private Integer dogAge;
    private String breed;
    private Double weight;
    private DogSize dogSize;
    private String dongAddress;
    private Double distance;
    private String date;
    private String startTime;
    private String endTime;
    private String locationNegotiationYn;
    private String suppliesProvidedYn;
    private String preMeetAvailableYn;
    private String walkNote;
    private String walkRequest;
    private String additionalRequest;
    private String ownerName;
    private Integer ownerAge;
    private Gender ownerGender;
    private String ownerProfile;
}
