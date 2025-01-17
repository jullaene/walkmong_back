// Firebase 스크립트 로드
importScripts('https://www.gstatic.com/firebasejs/9.22.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.22.0/firebase-messaging-compat.js');

// Firebase 초기화
firebase.initializeApp({
    apiKey: "[[${apiKey}]]",
    authDomain: "[[${authDomain}]]",
    projectId: "[[${projectId}]]",
    storageBucket: "[[${storageBucket}]]",
    messagingSenderId: "[[${messagingSenderId}]]",
    appId: "[[${appId}]]"
});

// Firebase Messaging 초기화
const messaging = firebase.messaging();

// 백그라운드 메시지 처리
messaging.onBackgroundMessage((payload) => {
    console.log('[firebase-messaging-sw.js] Received background message: ', payload);

    const notificationTitle = payload.data.title;
    const notificationBody = payload.data.body;

    // 로그로 데이터 페이로드 확인
    console.log('[firebase-messaging-sw.js] Notification Title:', notificationTitle);
    console.log('[firebase-messaging-sw.js] Notification Body:', notificationBody);

    const notificationOptions = {
        body: notificationBody,
        // icon: '/firebase-logo.png' // 원하는 아이콘 경로
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
