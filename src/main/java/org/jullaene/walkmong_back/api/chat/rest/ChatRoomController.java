package org.jullaene.walkmong_back.api.chat.rest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.WalkmongBackApplication;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatHistoryResponseDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatMessageResponseDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatRoomListResponseDto;
import org.jullaene.walkmong_back.api.chat.service.ChatRoomService;

import org.jullaene.walkmong_back.api.member.repository.MemberRepository;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatroom")

public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ApplyService applyService;
    private final MemberRepository memberRepository;
    private final BoardService boardService;

    //채팅방 생성
    @PostMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Long>>createRoom(@PathVariable(value = "boardId") Long boardId){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(chatRoomService.createRoom(boardId)));
    }

    //채팅방에 있는 기존 대화 내역 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<BasicResponse<List<ChatHistoryResponseDto>>> getChatHistory(@PathVariable(value="roomId") Long roomId){
        return ResponseEntity.ok(BasicResponse.ofSuccess(chatRoomService.getChatHistory(roomId)));
    }

    //지원|의뢰 && 매칭상태에 따른 채팅방 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<ChatRoomListResponseDto>>> getChatRoomList(@RequestParam("record") String record,
                                                                                        @RequestParam("status") WalkMatchingStatus status){
         List<ChatRoomListResponseDto> chatRoomListResponseDto = chatRoomService.getChatRoomList(record, status);

        return ResponseEntity.ok(BasicResponse.ofSuccess(chatRoomListResponseDto));
    }

    //채팅방 입장
    @MessageMapping("/message/enter")
    public void enter (@Payload ChatMessageRequestDto message) {
        log.info("채팅방 입장");
        ChatMessageResponseDto response = chatRoomService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), response);
    }

    //메세지 전송
    @MessageMapping("/message/send")
    public void sendMessage(@Payload ChatMessageRequestDto chat) {
        ChatMessageResponseDto response = chatRoomService.saveMessage(chat);
        messagingTemplate.convertAndSend("/sub/chat/room/" + chat.getRoomId(), response);
    }

    // 채팅방 퇴장
    @MessageMapping("/message/leave")
    public void leave (@Payload ChatMessageRequestDto message) {
        ChatMessageResponseDto response = chatRoomService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), response);
    }
}
