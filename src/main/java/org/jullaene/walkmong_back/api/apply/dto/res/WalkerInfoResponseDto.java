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
//산책자 정보+지원정보
public class WalkerInfoResponseDto {
    private String name;
    private Integer age;
    private Gender gender;
    private String profile;
    private String walkerDongAddress; //노원구 공릉동
    private String dongAddress;
    private String roadAddress; //강남구 학동로 508
    private String addressDetail; //스타벅스 앞
    private String addressMemo;
    private String poopBagYn;
    private String muzzleYn;
    private String dogCollarYn;
    private String preMeetingYn;
    private String memoToOwner;

}
