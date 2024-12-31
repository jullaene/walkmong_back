package org.jullaene.walkmong_back.api.chat.rest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.chat.dto.req.ChatMessageRequestDto;
import org.jullaene.walkmong_back.api.chat.dto.res.ChatHistoryResponseDto;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chatroom")

public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;
    private final ApplyService applyService;
    private final MemberRepository memberRepository;

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
                                                                                        @RequestParam("status")MatchingStatus status){
         /*
        record
        applied: 지원한 산책
        requested: 의뢰한 산책
        all: 전체
        */

         /*
        <status>
        PENDING: 매칭중
        CONFIRMED: 매칭확정
        REJECTED: 매칭취소
         */
        List<ChatRoomListResponseDto> chatRoomListResponseDto=null;
        if (record.equals("applied")){
            chatRoomListResponseDto=applyService.getAllChatListWithStatus(status);

            //공백으로 설정한 상대방 이름을 실명으로 전환
            for (ChatRoomListResponseDto dto:chatRoomListResponseDto){
                Long memberId=dto.getChatTarget();
                log.info("상대방 아이디 {}",memberId);
                dto.setTargetName(memberRepository.findNickNameByMemberId(memberId));
            }
        }

        return ResponseEntity.ok(BasicResponse.ofSuccess(chatRoomListResponseDto));
    }

    //채팅방 입장
    // pub/enterUser/를 통해 publish
    @MessageMapping("/enterUser")
    public void enterUser(@Payload ChatMessageRequestDto chat, StompHeaderAccessor headerAccessor) {
        log.info("채팅방 입장");
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
