package org.jullaene.walkmong_back.api.chat.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jullaene.walkmong_back.api.chat.domain.enums.MessageType;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {
    private final MessageType type;
    private final Long roomId;
    private final String senderNm;
    private final String message;
    private final LocalDateTime createdAt;

    @Builder
    public ChatMessageResponseDto(MessageType type, Long roomId, String senderNm, String message, LocalDateTime createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.senderNm = senderNm;
        this.message = message;
        this.createdAt = createdAt;
    }
}
