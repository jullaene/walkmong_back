package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDateTime;

//지원한 산책
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AppliedInfoResponseDto extends RecordResponseDto {
    private String dogName;
    private Gender dogGender;
    private String dogProfile;
    private String dongAddress;
    private String addressDetail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double distance;
}
