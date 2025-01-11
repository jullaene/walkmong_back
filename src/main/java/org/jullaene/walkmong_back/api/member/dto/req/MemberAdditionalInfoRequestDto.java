package org.jullaene.walkmong_back.api.member.dto.req;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class MemberAdditionalInfoRequestDto {

    @Size(min = 3, max = 16)
    private String nickname;

    private String name;

    private Gender gender;

    private LocalDate birthDate;

    private String phone;

    private MultipartFile profile;
}

