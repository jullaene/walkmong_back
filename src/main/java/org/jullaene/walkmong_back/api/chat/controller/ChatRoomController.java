package org.jullaene.walkmong_back.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.chat.dto.ChatMessageRequestDto;
import org.jullaene.walkmong_back.api.chat.service.ChatRoomService;

import org.jullaene.walkmong_back.common.BasicResponse;
import org.jullaene.walkmong_back.common.user.CustomUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chatroom")

public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;

    //채팅방 생성
    @PostMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Long>>createRoom(@PathVariable(value = "boardId") Long boardId){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(chatRoomService.createRoom(boardId)));

    }

    //채팅방 입장
    // pub/enterUser를 통해 publish
    @MessageMapping("/enterUser")
    public void enterUser(@Payload ChatMessageRequestDto chat,
                          StompHeaderAccessor headerAccessor) {
        SecurityContextHolder.getContext().setAuthentication((Authentication) headerAccessor.getUser());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomNumber(), chat);
    }

    //메세지 전송
    // pub/sendMessage를 통해 publish
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageRequestDto chat, StompHeaderAccessor headerAccessor) {
        SecurityContextHolder.getContext().setAuthentication((Authentication) headerAccessor.getUser());

        // 현재 인증된 사용자 정보 가져오기
        Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetail userDetails = (CustomUserDetail) principal;

        chatRoomService.saveMessage(chat,userDetails.getMember().getMemberId());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomNumber(), chat);
    }
}
