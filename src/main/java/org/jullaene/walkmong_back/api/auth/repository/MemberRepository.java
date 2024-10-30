package org.jullaene.walkmong_back.api.auth.repository;

import java.util.Optional;
import org.jullaene.walkmong_back.api.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);
}
