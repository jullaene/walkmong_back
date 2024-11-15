package org.jullaene.walkmong_back.api.member.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.dto.res.AddressResponseDto;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
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
}
