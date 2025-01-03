package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;

//산책 지원자 dto
@Getter
@AllArgsConstructor
public class ApplicantInfoResponseDto {
    private String applicantName;
    private String applicantProfile;
    private Integer applicantAge;
    private Gender applicantGender;
    private String applicantDongAddress;
    private String applicantRoadAddress;
    private Integer applicantRate;
}
