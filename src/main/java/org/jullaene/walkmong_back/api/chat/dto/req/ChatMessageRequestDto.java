package org.jullaene.walkmong_back.api.chat.dto.req;

import lombok.*;
import org.jullaene.walkmong_back.api.chat.domain.enums.MessageType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private String userName; //발신자
    private String msg; //메세지
    private Long roomId;
    private MessageType messageType;

    @Builder
    public ChatMessageRequestDto(String userName, String msg, Long roomId) {
        this.msg = msg;
        this.userName = userName;
        this.roomId=roomId;
    }
}
