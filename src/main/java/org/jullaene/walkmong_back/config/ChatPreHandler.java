package org.jullaene.walkmong_back.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.member.service.MemberService;
import org.jullaene.walkmong_back.common.jwt.JwtAuthenticationFilter;
import org.jullaene.walkmong_back.common.utils.JwtTokenUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenUtil jwtTokenUtil;

    //CONNECT,SUBSCRIBE,SEND 모두 거쳐간다
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor =  MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        //연결일 때만 토큰 검증하기
        if (headerAccessor.getCommand().equals(StompCommand.CONNECT) ){
            String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
           headerAccessor.setUser(jwtTokenUtil.getAuthentication(token));
            SecurityContextHolder.getContext().setAuthentication((Authentication) headerAccessor.getUser());
        }
        return message;

    }
}
