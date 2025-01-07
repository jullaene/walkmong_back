package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BasicResponse<MemberResponseDto>> getMemberInfo () {
        return ResponseEntity.ok(BasicResponse.ofSuccess(memberService.getMemberInfo()));
    }
}
