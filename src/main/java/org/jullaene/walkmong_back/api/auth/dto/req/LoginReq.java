package org.jullaene.walkmong_back.api.auth.dto.req;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginReq {
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,16}$")
    private String password;

}
