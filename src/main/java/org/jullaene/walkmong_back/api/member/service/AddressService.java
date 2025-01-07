package org.jullaene.walkmong_back.api.member.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Address;
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

    public List<AddressResponseDto> getAddresses(Long memberId) {
        List<Address> addresses = addressRepository.findByMemberIdAndDelYn(memberId, "N");
        return addresses.stream()
                .map(Address::toAddressResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeBasicAddress(Long memberId, Long addressId) {
        if (!addressRepository.existsByAddressIdAndMemberIdAndDelYn(addressId, memberId, "N")) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorType.ACCESS_DENIED);
        }

        List<Address> addresses = addressRepository.findByMemberIdAndDelYn(memberId, "N");

        // 모든 주소 다 기본에서 제외
        addresses.forEach(address -> address.changeBasicAddressYn("N"));

        // 주어진 addressId를 기본 주소로 설정
        addresses.stream()
                .filter(address -> address.getAddressId().equals(addressId))
                .forEach(address -> address.changeBasicAddressYn("Y"));
    }
}
