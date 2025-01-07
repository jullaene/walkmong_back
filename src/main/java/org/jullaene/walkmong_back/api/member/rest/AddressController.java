package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.AddressReq;
import org.jullaene.walkmong_back.api.member.dto.res.AddressResponseDto;
import org.jullaene.walkmong_back.api.member.service.AddressService;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address", description = "주소 정보 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<AddressResponseDto>>> getAddressList() {
        Member member = memberService.getMemberFromUserDetail();
        return ResponseEntity.ok(BasicResponse.ofSuccess(addressService.getAddresses(member.getMemberId())));
    }

    @PostMapping("")
    public ResponseEntity<BasicResponse<Long>> careteAddress (@RequestBody AddressReq addressReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(addressService.createAddress(addressReq)));
    }
}
