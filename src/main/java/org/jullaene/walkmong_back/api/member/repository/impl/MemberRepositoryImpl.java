package org.jullaene.walkmong_back.api.member.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.domain.QAddress;
import org.jullaene.walkmong_back.api.member.domain.QMember;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;
import org.jullaene.walkmong_back.api.member.repository.MemberRepositoryCustom;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public MemberResponseDto getMemberInfo(Long memberId, String delYn) {
        QMember member = QMember.member;
        QAddress address = QAddress.address;

        return queryFactory.select(
                Projections.constructor(
                        MemberResponseDto.class,
                        member.nickname,
                        address.addressId,
                        address.dongAddress,
                        member.introduce,
                        member.name,
                        member.gender,
                        member.birthDate,
                        member.phone,
                        member.profile
                )
        )
                .from(member)
                .join(address)
                .on(address.memberId.eq(memberId)
                        .and(address.basicAddressYn.eq("Y"))
                        .and(address.delYn.eq(delYn)))
                .where(member.memberId.eq(memberId)
                        .and(member.delYn.eq(delYn)))
                .fetchOne();
    }
}
