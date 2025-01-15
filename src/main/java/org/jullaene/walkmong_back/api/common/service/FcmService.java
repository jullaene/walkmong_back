package org.jullaene.walkmong_back.api.common.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.common.domain.FcmToken;
import org.jullaene.walkmong_back.api.common.repository.FcmTokenRepository;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;

    // 단일 사용자에게 알림 전송
    @Transactional(readOnly = true)
    public String sendNotification(Long accountId, String title, String body) {
        FcmToken fcmToken = fcmTokenRepository.findByMemberIdAndDelYn(accountId, "N")
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorType.INVALID_FCM_TOKEN));

        return sendNotificationToToken(fcmToken.getToken(), title, body);
    }

    // 다중 사용자에게 알림 전송
    @Transactional(readOnly = true)
    public BatchResponse sendNotificationToMultipleUsers(List<Long> accountIds, String title, String body) {
        List<FcmToken> tokens = fcmTokenRepository.findAllByMemberIdInAndDelYn(accountIds, "N");

        String requestId = UUID.randomUUID().toString();
        log.info("사용자 " + tokens.size() + "명에게 알림 전송");
        log.info("제목: " + title);
        log.info("내용: " + body);


        List<String> tokenValues = tokens.stream()
                .map(FcmToken::getToken)
                .toList();
        log.info("토큰: " + tokenValues.get(0));

        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(tokenValues)
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            log.info("RequestID: {} - 전송 성공 수: {}, 실패 수: {}",
                    requestId,
                    response.getSuccessCount(),
                    response.getFailureCount());

            return response;
        } catch (FirebaseMessagingException e) {
            log.error("FCM 다중 발송 실패: " + e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.FAIL_FCM_SEND);
        }
    }

    private String sendNotificationToToken(String token, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putData("title", title)
                    .putData("body", body)
                    .build();

            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 발송 실패: " + e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.FAIL_FCM_SEND);
        }
    }
}
