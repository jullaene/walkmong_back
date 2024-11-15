package org.jullaene.walkmong_back.api.board.dto.res;

import lombok.Builder;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

public record BoardResponseDto(String startTime, String endTime, String matchingYn,
                               String dogName, String dogProfile,
                               Gender dogGender, String breed, Double weight,
                               DogSize dogSize, String content,
                               String dongAddress, Double distance) {

    @Builder
    public BoardResponseDto {
    }
}