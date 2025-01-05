package org.jullaene.walkmong_back.api.member.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.user.CustomUserDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static org.jullaene.walkmong_back.common.exception.ErrorType.INVALID_USER;
import static org.jullaene.walkmong_back.common.exception.ErrorType.USER_NOT_AUTHENTICATED;

@Slf4j
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
        throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.USER_NOT_AUTHENTICATED);
    }

    @Transactional
    public Long registerWalkingExperience(@Valid WalkExperienceReq walkExperienceReq) {
        // token으로 멤버 체크
        Member memberForCheck = getMemberFromUserDetail();

        // 실제 멤버를 db에서 가져오기
        Member member = memberRepository.findById(memberForCheck.getMemberId())
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_USER));

        member.addWalkingExperience(walkExperienceReq);

        return member.getMemberId();
    }
}
