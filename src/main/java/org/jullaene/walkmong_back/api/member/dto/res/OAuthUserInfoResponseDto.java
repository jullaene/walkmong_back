package org.jullaene.walkmong_back.api.member.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 소셜 로그인 유저 정보 DTO
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
public class OAuthUserInfoResponseDto {
    // 고유ID
    @JsonProperty("sub")
    private String subject;

    @JsonProperty("email")
    private String email;
}
