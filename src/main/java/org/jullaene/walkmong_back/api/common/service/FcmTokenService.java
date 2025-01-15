package org.jullaene.walkmong_back.api.common.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.common.domain.FcmToken;
import org.jullaene.walkmong_back.api.common.repository.FcmTokenRepository;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberService memberService;

    @Transactional
    public Long saveOrUpdateToken(String token) {
        Member member = memberService.getMemberFromUserDetail();

        FcmToken savedFcmToken = fcmTokenRepository.findByMemberIdAndDelYn(member.getMemberId(), "N")
                .map(fcmToken -> {
                    fcmToken.updateToken(token);
                    return fcmToken;
                })
                .orElseGet(() -> fcmTokenRepository.save(new FcmToken(member.getMemberId(), token)));

        return savedFcmToken.getFcmTokenId();
    }

    @Transactional
    public String removeToken() {
        Member member = memberService.getMemberFromUserDetail();

        fcmTokenRepository.findByMemberIdAndDelYn(member.getMemberId(), "N")
                .ifPresent(FcmToken::delete);

        return "SUCCESS";
    }
}
