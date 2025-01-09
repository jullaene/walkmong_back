package org.jullaene.walkmong_back.api.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.jullaene.walkmong_back.common.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {

            String accessToken = extractAccessToken(accessor);

            if (jwtTokenUtil.verify(accessToken) != null) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
            }
        }

        return message;
    }

    private String extractAccessToken (StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader("accessToken");
        if (accessToken != null && accessToken.startsWith(BEARER_PREFIX)) {
            accessToken = accessToken.substring(7); // "Bearer " 접두사 제거
        }
        return accessToken;
    }
}
