package org.jullaene.walkmong_back.config;

import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.chat.handler.ChatPreHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatPreHandler chatPreHandler;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    /*한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는데 사용될 메시지 브로커*/
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //sub으로 시작되는 요청을 구독한 모든 사용자들에게 메시지를 broadcast한다.
        registry.enableSimpleBroker("/sub");

        // pub로 시작되는 메시지는 message-handling methods로 라우팅된다.
        registry.setApplicationDestinationPrefixes("/pub");
    }

    //clinetInboundChanel: client에서 서버로 들어오는 요청을 전달한다
    //토큰 관련 로직을 처리한다
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler)
                .taskExecutor()
                .corePoolSize(1)
                .maxPoolSize(10);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(128 * 1024)
                .setSendBufferSizeLimit(512 * 1024)
                .setSendTimeLimit(20000);
    }




}
