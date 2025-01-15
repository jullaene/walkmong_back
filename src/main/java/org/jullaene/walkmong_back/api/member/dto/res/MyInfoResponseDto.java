package org.jullaene.walkmong_back.api.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MyInfoResponseDto {
    private final String nickname;
    private final Long addressId;
    private final String dongAddress;
    private final String introduction;
    private final String name;
    private final Gender gender;
    private final LocalDate birthDate;
    private final String phone;
    private final String profile;

}
