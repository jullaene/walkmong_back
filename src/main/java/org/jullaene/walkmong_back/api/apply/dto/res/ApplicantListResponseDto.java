package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ApplicantListResponseDto {
    private String applicantName;
    private String applicantProfile;
    private Integer applicantAge;
    private Gender applicantGender;
    private String dongAddress;
    private String roadAddress;
    private Integer applicantRate;
}
