package org.jullaene.walkmong_back.api.member.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
}
