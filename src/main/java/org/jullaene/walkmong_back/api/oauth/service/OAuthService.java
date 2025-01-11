package org.jullaene.walkmong_back.api.oauth.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.domain.Member;
import org.jullaene.walkmong_back.api.member.domain.enums.Provider;
import org.jullaene.walkmong_back.api.member.dto.res.OAuthLoginResponseDto;
import org.jullaene.walkmong_back.api.member.dto.res.OAuthUserInfoResponseDto;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.utils.JwtTokenUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public OAuthLoginResponseDto handleSocialLogin(OAuthUserInfoResponseDto userInfoDto, Provider provider) {

        String email = userInfoDto.getEmail();
        String providerId = userInfoDto.getSubject();

        Optional<Member> memberOptional = memberRepository.findByProviderIdAndProviderAndDelYn(providerId, provider, "N");
        Optional<Member> emailMemberOptional = memberRepository.findByEmailAndDelYn(email, "N");

        Member member;
        boolean isNewUser = false; // 새로운 회원 여부 플래그

        if (memberOptional.isPresent()) {
            // 이미 동일한 소셜 계정으로 가입된 경우
            member = memberOptional.get();
        } else if (emailMemberOptional.isPresent()) {
            // 동일한 이메일로 기존 회원이 존재하는 경우
            member = emailMemberOptional.get();

            // 기존 회원 정보를 소셜 로그인 정보로 연결
            if (member.getProvider() == null && member.getProviderId() == null) {
                member.linkSocialAccount(providerId, provider);
                memberRepository.save(member);
            } else {
                // 기존 회원이 다른 소셜 계정을 사용 중일 경우 예외 처리
                throw new CustomException(HttpStatus.CONFLICT, ErrorType.EMAIL_ALREADY_REGISTERED);
            }
        } else {
            // 새로운 회원 생성
            member = new Member(email, providerId, provider);
            memberRepository.save(member);
            isNewUser = true; // 플래그 설정
        }

        String accessToken = jwtTokenUtil.createToken(member.getEmail());
        String refreshToken = jwtTokenUtil.createRefreshToken(member.getEmail());

        redisTemplate.opsForValue().set(
                member.getEmail(),
                refreshToken,
                jwtTokenUtil.getRefreshTokenExpirationMillis(),
                TimeUnit.MILLISECONDS
        );

        return new OAuthLoginResponseDto(accessToken, refreshToken, isNewUser);
    }
}
