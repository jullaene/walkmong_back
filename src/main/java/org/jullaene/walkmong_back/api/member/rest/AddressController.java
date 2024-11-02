package org.jullaene.walkmong_back.api.member.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.service.AddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Address", description = "주소 정보 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;
}
