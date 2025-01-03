package org.jullaene.walkmong_back.api.apply.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//지원서의 내용
@Getter
@AllArgsConstructor
@Builder
public class ApplyInfoResponseDto {
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
