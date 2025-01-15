package org.jullaene.walkmong_back.api.common.repository;

import org.jullaene.walkmong_back.api.common.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMemberIdAndDelYn(Long memberId, String delYn);

    List<FcmToken> findAllByMemberIdInAndDelYn(List<Long> memberIds, String delYn);
}
