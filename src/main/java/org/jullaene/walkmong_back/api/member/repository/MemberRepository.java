package org.jullaene.walkmong_back.api.member.repository;

import java.util.Optional;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.domain.enums.Provider;
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

    Optional<Member> findByMemberIdAndDelYn(Long memberId, String delYn);

    boolean existsByEmailOrNicknameAndDelYn(String email, String nickname, String delYn);

    Optional<Member> findByProviderIdAndProviderAndDelYn(String providerId, Provider provider, String delYn);

    Optional<Member> findByEmailAndDelYn(String email, String delYn);
}
