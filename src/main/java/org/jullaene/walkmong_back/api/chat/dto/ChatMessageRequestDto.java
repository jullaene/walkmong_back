package org.jullaene.walkmong_back.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    public enum MessageType{
        ENTER, TALK
    }
    private String userName; //발신자
    private String msg; //메세지
    private Long roomNumber; //방번호
    private MessageType messageType;

    @Builder
    public ChatMessageRequestDto(String userName, String msg, Long roomNumber) {
        this.msg = msg;
        this.userName = userName;
        this.roomNumber=roomNumber;
    }
}
