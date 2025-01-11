package org.jullaene.walkmong_back.api.oauth.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.enums.Provider;
import org.jullaene.walkmong_back.api.member.dto.res.OAuthLoginResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.OAuthUserInfoResponseDto;
import org.jullaene.walkmong_back.api.oauth.service.OAuthService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.jullaene.walkmong_back.api.oauth.service.AppleTokenService;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "OAuth 인증 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final AppleTokenService appleTokenService;
    private final OAuthService oAuthService;

    // redirect url로 authorizaion code 획득
    @PostMapping("/apple/callback")
    public ResponseEntity<BasicResponse<OAuthLoginResponseDto>> handleAppleCallback(@RequestParam Map<String, String> params) {
        String authorizationCode = params.get("code");

        if (authorizationCode == null || authorizationCode.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorType.MISSING_AUTHORIZATION_CODE);
        }
        // 1. Apple 토큰 API 호출 및 검증
        OAuthUserInfoResponseDto userInfo = appleTokenService.processToken(authorizationCode);

        // 2. 로그인 or 회원 가입 진행
        OAuthLoginResponseDto loginResponse = oAuthService.handleSocialLogin(userInfo, Provider.APPLE);

        // 3. 응답 반환
        return ResponseEntity.ok(BasicResponse.ofSuccess(loginResponse));
    }
}
