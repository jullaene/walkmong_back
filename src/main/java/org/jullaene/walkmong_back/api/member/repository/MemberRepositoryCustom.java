package org.jullaene.walkmong_back.api.member.repository;

import org.jullaene.walkmong_back.api.member.dto.common.WalkingBasicInfo;
import org.jullaene.walkmong_back.api.member.dto.res.MemberResponseDto;

public interface MemberRepositoryCustom {
    MemberResponseDto getMemberInfo(Long memberId, String delYn);

    WalkingBasicInfo getWalkingInfo(Long memberId, String delYn);
}
