package org.jullaene.walkmong_back.api.member.service;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
}
