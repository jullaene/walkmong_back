package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jullaene.walkmong_back.api.chat.domain.enums.MessageType;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {
    private MessageType type;
    private Long roomId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessageResponseDto(MessageType type, Long roomId, Long senderId, String message, LocalDateTime createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.createdAt = createdAt;
    }
}
