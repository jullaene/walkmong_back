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

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, StompHeaderAccessor.class);

        try {
            if (StompCommand.CONNECT.equals(Objects.requireNonNull(accessor).getCommand()) ||
                    StompCommand.SEND.equals(accessor.getCommand())) {
                String accessToken = extractAccessToken(accessor);

                if (accessToken == null || accessToken.isEmpty()) {
                    log.error("No access token provided");
                    throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
                }

                if (!jwtTokenUtil.validateToken(accessToken)) {
                    log.error("Invalid token");
                    throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.ACCESS_DENIED);
                }

                // 토큰 검증 성공 시 Authentication 설정
                Authentication auth = jwtTokenUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
            }

            return message;
        } catch (CustomException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in ChatPreHandler: ", e);
            return message;
        }
    }

    private String extractAccessToken (StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader("Authorization");

        if (accessToken != null && accessToken.startsWith(BEARER_PREFIX)) {
            accessToken = accessToken.substring(7); // "Bearer " 접두사 제거
        }

        return accessToken;
    }
}
