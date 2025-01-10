package org.jullaene.walkmong_back.api.member.dto.req;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberCreateReq {
    @Pattern(regexp = "[a-zA-Z0-9]+[@][a-zA-Z0-9]+[.]+[a-zA-Z]+[.]*[a-zA-Z]*")
    private String email;

    @Size(min = 3, max = 16)
    private String nickname;

    @Pattern(regexp = "^(?=(.*[a-zA-Z].*){2,}|.*\\d.*|.*[@$!%*?&].*)(?=.*[a-zA-Z\\d@$!%*?&])[a-zA-Z\\d@$!%*?&]{8,20}$")
    private String password;

    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private MultipartFile profile;
    private String phone;

    public Member toEntity(String profileUrl, String password) {
        return Member.builder()
                .memberCreateReq(this)
                .password(password)
                .profileUrl(profileUrl)
                .build();
    }
}
