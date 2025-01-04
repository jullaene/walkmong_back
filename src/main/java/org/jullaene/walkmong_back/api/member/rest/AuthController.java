package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.dto.req.LoginReq;
import org.jullaene.walkmong_back.api.member.dto.req.MemberCreateReq;
import org.jullaene.walkmong_back.api.member.dto.res.LoginRes;
import org.jullaene.walkmong_back.api.member.service.AuthService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<BasicResponse<Long>> signUp(@Valid @RequestBody MemberCreateReq memberCreateReq){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(authService.createAccount(memberCreateReq)));
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<BasicResponse<LoginRes>> login(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.login(loginReq)));
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
    @PostMapping("/email/duplicate")
    public ResponseEntity<BasicResponse<String>> emailDuplicate (@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.duplicateEmail(email)));
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
    @PostMapping("/nickname/duplicate")
    public ResponseEntity<BasicResponse<String>> nicknameDuplicate (@RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.duplicateNickname(nickname)));
    }

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 요청")
    @PostMapping("/email/code/request")
    public ResponseEntity<BasicResponse<String>> requestVerification(@RequestParam(name = "email") String email) {
        try {
            return ResponseEntity.ok(BasicResponse.ofSuccess(authService.requestEmailVerification(email)));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_SERVER);
        }
    }

    @PostMapping("/email/code/verify")
    public ResponseEntity<BasicResponse<String>> verifyCode(@RequestParam(name = "email") String email,
                                             @RequestParam(name = "code") String code) {
        boolean isVerified = authService.verifyCode(email, code);

        if (isVerified) {
            return ResponseEntity.ok(BasicResponse.ofSuccess("인증에 성공했습니다."));
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_VERIFICATION_CODE);
        }
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 통해 Access Token을 재발급받습니다.")
    @PostMapping("/reissue/accesstoken")
    public ResponseEntity<BasicResponse<String>> reissueToken(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.reissueTokens(email, refreshToken)));
    }
}