package org.jullaene.walkmong_back.api.common.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FcmPageController {

    @Value("${firebase.config.apiKey}")
    private String apiKey;

    @Value("${firebase.config.authDomain}")
    private String authDomain;

    @Value("${firebase.config.projectId}")
    private String projectId;

    @Value("${firebase.config.storageBucket}")
    private String storageBucket;

    @Value("${firebase.config.messagingSenderId}")
    private String messagingSenderId;

    @Value("${firebase.config.appId}")
    private String appId;

    @Value("${firebase.config.vapidKey}")
    private String vapidKey;
    /**
     * fcmToken 테스트 전
     * fcmToken 발급을 위한 웹
     * */
    @GetMapping("/fcm")
    public String fcmPage(Model model) {
        model.addAttribute("apiKey", apiKey);
        model.addAttribute("authDomain", authDomain);
        model.addAttribute("projectId", projectId);
        model.addAttribute("storageBucket", storageBucket);
        model.addAttribute("messagingSenderId", messagingSenderId);
        model.addAttribute("appId", appId);
        model.addAttribute("vapidKey", vapidKey);
        return "fcm";
    }


    /**
     * Service Worker Script 반환
     */
    @GetMapping(value = "/firebase-messaging-sw.js", produces = "application/javascript")
    @ResponseBody
    public String firebaseMessagingSw() {
        return String.format("""
        importScripts('https://cdnjs.cloudflare.com/ajax/libs/firebase/9.22.0/firebase-app-compat.min.js');
        importScripts('https://cdnjs.cloudflare.com/ajax/libs/firebase/9.22.0/firebase-messaging-compat.min.js');
        
        firebase.initializeApp({
            apiKey: "%s",
            authDomain: "%s",
            projectId: "%s",
            storageBucket: "%s",
            messagingSenderId: "%s",
            appId: "%s"
        });
        
        const messaging = firebase.messaging();
        
        messaging.onBackgroundMessage((payload) => {
            console.log('[firebase-messaging-sw.js] Received background message:', payload);
            
            const notificationTitle = payload.data.title;
            const notificationBody = payload.data.body;
        
            const notificationOptions = {
                body: notificationBody,
                // icon: '/firebase-logo.png' // 원하는 아이콘 경로
            };
            
            return self.registration.showNotification(notificationTitle, notificationOptions);
        });
        """,
                apiKey,
                authDomain,
                projectId,
                storageBucket,
                messagingSenderId,
                appId
        );
    }

}
