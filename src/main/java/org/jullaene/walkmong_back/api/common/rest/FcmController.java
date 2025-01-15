package org.jullaene.walkmong_back.api.common.rest;

import com.google.firebase.messaging.BatchResponse;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.common.dto.req.MultiNotificationReq;
import org.jullaene.walkmong_back.api.common.dto.req.NotificationReq;
import org.jullaene.walkmong_back.api.common.service.FcmService;
import org.jullaene.walkmong_back.api.common.service.FcmTokenService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class FcmController {
    private final FcmService fcmService;
    private final FcmTokenService fcmTokenService;

     /**
      * FCM 토큰 등록/업데이트
      * */
    @PostMapping("/token")
    public ResponseEntity<BasicResponse<Long>> registerToken(@RequestParam(value = "token") String token) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(fcmTokenService.saveOrUpdateToken(token)));
    }

    /**
     * 단일 사용자에게 알림 전송
     * */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationReq request) {
        String messageId = fcmService.sendNotification(
                request.getMemberId(),
                request.getTitle(),
                request.getBody()
        );

        return ResponseEntity.ok(messageId);
    }

     /**
      * 다중 사용자에게 알림 전송
      * */
    @PostMapping("/send/users")
    public ResponseEntity<BatchResponse> sendToMultipleUsers(@RequestBody MultiNotificationReq request) {
        BatchResponse response = fcmService.sendNotificationToMultipleUsers(
                request.getMemberIds(),
                request.getTitle(),
                request.getBody()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * FCM Token 삭제 (soft delete)
     * */
    @DeleteMapping("/token")
    public ResponseEntity<BasicResponse<String>> markDeletedFcmToken () {
        return ResponseEntity.ok(BasicResponse.ofSuccess(fcmTokenService.removeToken()));
    }


}
