package org.jullaene.walkmong_back.api.member.repository;

import java.util.Optional;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    @Query("SELECT m.nickname FROM Member m WHERE m.memberId = :memberId")
    String findNickNameByMemberId(Long memberId);
}
