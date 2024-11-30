package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.jullaene.walkmong_back.api.member.dto.req.LoginReq;
import org.jullaene.walkmong_back.api.member.dto.req.MemberCreateReq;
import org.jullaene.walkmong_back.api.member.service.AuthService;
import org.jullaene.walkmong_back.common.BasicResponse;
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
    public ResponseEntity<BasicResponse<String>> login(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.login(loginReq)));
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
    @PostMapping("/email/duplicate")
    public ResponseEntity<BasicResponse<String>> emailDuplicate (@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(authService.duplicateEmail(email)));
    }


}