package org.jullaene.walkmong_back.api.member.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.AddressReq;
import org.jullaene.walkmong_back.api.member.dto.res.AddressResponseDto;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final MemberService memberService;

    public List<AddressResponseDto> getAddresses(Long memberId) {
        List<Address> addresses = addressRepository.findByMemberIdAndDelYn(memberId, "N");
        return addresses.stream()
                .map(Address::toAddressResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createAddress(AddressReq addressReq) {
        Member member = memberService.getMemberFromUserDetail();

        if (addressRepository.existsByMemberIdAndBasicAddressYnAndDelYn(member.getMemberId(), "Y", "N")) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorType.DUPLICATED_BASIC_ADDRESS);
        }

        Address address = addressReq.toEntity(member.getMemberId());

        return addressRepository.save(address).getAddressId();
    }
}
