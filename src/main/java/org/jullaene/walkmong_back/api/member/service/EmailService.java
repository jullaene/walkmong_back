package org.jullaene.walkmong_back.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    private static final int CODE_EXPIRATION_MINUTES = 10;
    private static final int CODE_LENGTH = 6;
    @Value("${smtp.mail.username}")
    private String managerName;

    /**
     * 인증번호 생성 및 redis에 저장
     * */
    public void sendVerificationCode(String email) {
        String redisKey = "email_verification:" + email;

        // 기존 키가 존재하면 삭제
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            redisTemplate.delete(redisKey);
        }

        // 새 인증번호 생성 및 처리
        String verificationCode = generateVerificationCode();
        redisTemplate.opsForValue().set(
                redisKey,
                verificationCode,
                Duration.ofMinutes(CODE_EXPIRATION_MINUTES)
        );

        // 이메일 발송
        sendVerificationEmail(email, verificationCode);
    }

    /**
     * 인증번호 검증
     * */
    public boolean verifyCode(String email, String userInputCode) {
        String redisKey = "email_verification:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        // 코드 검증
        if (storedCode != null && storedCode.equals(userInputCode)) {
            // 인증 성공 시 Redis에서 삭제
            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

    /**
     * 인증번호 생성
     * */
    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        return random.ints(CODE_LENGTH, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    /**
     * 이메일로 인증번호 전송
     * */
    private void sendVerificationEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(managerName);
        message.setTo(to);
        message.setSubject("[WAlKMONG] 이메일 인증 코드");
        message.setText("인증 코드는 [" + verificationCode + "] 입니다. " +
                CODE_EXPIRATION_MINUTES + "분 이내에 입력해주세요.");

        mailSender.send(message);
    }
}
