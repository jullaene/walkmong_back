package org.jullaene.walkmong_back.api.oauth.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPrivateKey;
import java.util.Base64;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Component
public class AppleClientSecretGenerator {

    @Value("${apple.auth.team-id}")
    private String teamId;

    @Value("${apple.auth.client-id}")
    private String clientId;

    @Value("${apple.auth.key-id}")
    private String keyId;

    @Value("${apple.auth.key-path}")
    private String keyPath;

    @Value("${apple.auth.url}")
    private String appleUrl;

    /**
     * Generates a client secret (JWT) for Apple Sign-in.
     *
     * @return Generated client secret (JWT)
     */
    public String generateClientSecret() {
        try {
            PrivateKey privateKey = loadPrivateKey();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(keyId)
                    .build();

            long now = System.currentTimeMillis();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(teamId)
                    .claim("iat", new Date(now).getTime() / 1000) // 현재 시간
                    .expirationTime(new Date(now + 3600 * 1000)) // 1시간 뒤 만료
                    .audience(appleUrl)
                    .subject(clientId)
                    .build();

            // JWT 서명 생성
            SignedJWT signedJWT = new SignedJWT(header, claims);
            JWSSigner signer = new ECDSASigner((ECPrivateKey) privateKey);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.CLIENT_SECRET_GENERATION_ERROR);
        }
    }


    private PrivateKey loadPrivateKey() {
        try {
            // 1. 키 파일 읽기
            InputStream inputStream = new ClassPathResource(keyPath).getInputStream();
            byte[] keyBytes = inputStream.readAllBytes();
            String keyContent = new String(keyBytes, StandardCharsets.UTF_8);

            // 2. PEM 헤더와 푸터 제거
            keyContent = keyContent
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", ""); // 공백 제거

            // 3. Base64 디코딩
            byte[] decodedKey = Base64.getDecoder().decode(keyContent);

            // 4. PKCS8EncodedKeySpec을 사용하여 PrivateKey 생성
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.PRIVATE_KEY_LOAD_ERROR);
        }
    }
}
