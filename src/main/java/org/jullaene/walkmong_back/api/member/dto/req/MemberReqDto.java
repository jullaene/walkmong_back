package org.jullaene.walkmong_back.api.member.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberReqDto {
    private final String nickname;
    private final Long addressId;
    private final String introduction;
    private final String name;
    private final Gender gender;
    private final LocalDate birthDate;
    private final String phone;
    private final MultipartFile profile;

}
