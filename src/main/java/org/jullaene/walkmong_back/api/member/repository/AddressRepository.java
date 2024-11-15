package org.jullaene.walkmong_back.api.member.repository;

import org.jullaene.walkmong_back.api.member.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByMemberIdAndBasicAddressYn(Long memberId, String basicAddressYn);
}
