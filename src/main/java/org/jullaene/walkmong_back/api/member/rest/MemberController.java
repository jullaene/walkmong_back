package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.MemberAdditionalInfoRequestDto;
import org.jullaene.walkmong_back.api.member.dto.req.MemberReqDto;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.MyInfoResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.WalkingResponseDto;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "멤버 정보 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "산책추가정보등록", description = "산책추가정보등록")
    @PostMapping("/experience")
    public ResponseEntity<BasicResponse<Long>> registerWalkExperienceInfo(@Valid @RequestBody WalkExperienceReq walkExperienceReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.registerWalkingExperience(walkExperienceReq)));
    }

    @Operation(summary = "내 정보 조회", description = "내 정보를 조회하는 API입니다.")
    @GetMapping("/mypage")
    public ResponseEntity<BasicResponse<MyInfoResponseDto>> getMyInfo () {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.getMyInfo()));
    }

    @Operation(summary = "산책 관련 정보 조회", description = "산책 관련 정보를 조회하는 API입니다.")
    @GetMapping("/walking")
    public ResponseEntity<BasicResponse<WalkingResponseDto>> getWalkingInfo () {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.getWalkingInfo()));
    }

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 API입니다.")
    @GetMapping("/{memberId}")
    public ResponseEntity<BasicResponse<MemberResponseDto>> getMemberInfo (
            @PathVariable(name = "memberId") Long memberId
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.getMemberInfo(memberId)));
    }

    @Operation(summary = "내 정보 수정", description = "내 정보를 수정하는 API입니다.")
    @PutMapping("/mypage")
    public ResponseEntity<BasicResponse<Long>> getMemberInfo (@ModelAttribute MemberReqDto memberReqDto) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.updateMemberInfo(memberReqDto)));
    }

    @Operation(summary = "소셜 로그인 후 회원 정보 추가 등록", description = "소셜 로그인을 통해 회원 가입한 경우, 회원 정보를 추가로 입력하는 API입니다.")
    @PatchMapping("/additional-info")
    public ResponseEntity<BasicResponse<Member>> addAdditionalInfo(
            @Valid @RequestBody MemberAdditionalInfoRequestDto additionalInfoReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.addAdditionalInfo(additionalInfoReq)));
    }
}
