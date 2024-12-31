package org.jullaene.walkmong_back.api.chat.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.jullaene.walkmong_back.common.BaseEntity;

@NoArgsConstructor
@Getter
@Entity
@Table(name="chat")
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_id")
    private Long chatId;

    @Comment("방번호")
    private Long roomId;

    @Comment("송신자")
    private Long senderId;

    @Comment("메세지")
    private String message;

    @Builder
    public Chat(Long roomId, Long senderId,String message){
        this.roomId=roomId;
        this.senderId=senderId;
        this.message=message;
    }

}
