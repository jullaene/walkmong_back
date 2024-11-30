package org.jullaene.walkmong_back.api.member.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static org.jullaene.walkmong_back.common.exception.ErrorType.USER_NOT_AUTHENTICATED;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * CustomUserDetail에서 member 가져오기
     * */
    public Member getMemberFromUserDetail () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail customUserDetail) {
            return customUserDetail.getMember(); // member 객체 가져오기
        }
        throw new CustomException(HttpStatus.UNAUTHORIZED, USER_NOT_AUTHENTICATED);
    }

    public Long registerWalkingExperience(@Valid WalkExperienceReq walkExperienceReq) {
        getMemberFromUserDetail().addWalkingExperience(walkExperienceReq);

        return getMemberFromUserDetail().getMemberId();
    }
}
