package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//반려인이 산책자 지원서 조회
//산책자 정보+지원정보
public class WalkerInfoResponseDto {
    private ApplicantInfoResponseDto applicantInfoResponseDto;
    private ApplyInfoResponseDto applyInfoResponseDto;
}
