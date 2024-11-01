package org.jullaene.walkmong_back.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.auth.domain.Member;
import org.jullaene.walkmong_back.api.auth.dto.req.LoginReq;
import org.jullaene.walkmong_back.api.auth.dto.req.MemberCreateReq;
import org.jullaene.walkmong_back.api.auth.repository.MemberRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     */
    public String login(LoginReq loginReq) {
        Member member = findByAccountEmail(loginReq.getEmail());
        if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.WRONG_PASSWORD);
        }

        return jwtTokenUtil.createToken(member.getEmail());
    }

    /**
     * Account 등록
     */
    @Transactional
    public Long createAccount(MemberCreateReq memberCreateReq) {

        if (memberRepository.existsByEmail(memberCreateReq.getEmail())) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorType.ALREADY_EXIST_USER);
        }

        Member member = Member.builder()
                .email(memberCreateReq.getEmail())
                .nickname(memberCreateReq.getNickname())
                .password(passwordEncoder.encode(memberCreateReq.getPassword()))
                .build();

        return memberRepository.save(member).getMemberId();
    }

    /**
     * 이메일을 이용하여 Account 정보를 찾는 API
     */
    private Member findByAccountEmail(String email){
        return memberRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_USER));
    }

}
