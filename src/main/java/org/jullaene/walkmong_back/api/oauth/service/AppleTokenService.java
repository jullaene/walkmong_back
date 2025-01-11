package org.jullaene.walkmong_back.api.oauth.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import org.jullaene.walkmong_back.api.member.dto.res.OAuthUserInfoResponseDto;
import org.jullaene.walkmong_back.api.oauth.dto.AppleTokenInfoResponseDto;
import org.jullaene.walkmong_back.api.oauth.utils.AppleClientSecretGenerator;
import org.jullaene.walkmong_back.common.exception.CustomException;
import org.jullaene.walkmong_back.common.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AppleTokenService {

    @Value("${apple.auth.client-id}")
    private String clientId;

    @Value("${apple.auth.token-url}")
    private String tokenUrl;

    @Value("${apple.auth.key-url}")
    private String APPLE_PUBLIC_KEYS_URL;

    @Value("${apple.auth.url}")
    private String ISSUER;

    @Value("${apple.auth.redirect-uri}")
    private String redirectUri;

    private final AppleClientSecretGenerator appleClientSecretGenerator;

    public AppleTokenService(AppleClientSecretGenerator appleClientSecretGenerator) {
        this.appleClientSecretGenerator = appleClientSecretGenerator;
    }

    public OAuthUserInfoResponseDto processToken(String authorizationCode) {
        // 1. Client secret 생성
        String clientSecret = appleClientSecretGenerator.generateClientSecret();

        // 2. Apple 토큰 API 호출
        AppleTokenInfoResponseDto tokenResponse = getIdToken(clientSecret, authorizationCode);

        // 3. 토큰 검증 및 유저 정보 추출
        return validateToken(tokenResponse);
    }

    private AppleTokenInfoResponseDto getIdToken(String clientSecret, String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);

        // HTTP 요청 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Apple의 /auth/token API 호출
            ResponseEntity<AppleTokenInfoResponseDto> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, requestEntity, AppleTokenInfoResponseDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_GRANT);
            }
        } catch (HttpClientErrorException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_GRANT);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_SERVER);
        }
    }

    private OAuthUserInfoResponseDto validateToken(AppleTokenInfoResponseDto tokenResponse) {
        try {
            // 1. JWT Processor 설정
            String idToken = tokenResponse.getIdToken();

            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

            // 2. Apple 공개 키 가져오기
            JWKSet jwkSet = JWKSet.load(new URL(APPLE_PUBLIC_KEYS_URL));

            // 3. JWS 키 선택자 설정
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
            JWSVerificationKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);

            // 4. JWT Processor에 Key Selector 연결
            jwtProcessor.setJWSKeySelector(keySelector);

            // 5. ID Token 검증
            JWTClaimsSet claims = jwtProcessor.process(idToken, null);

            // 6. Claim 검증
            if (!ISSUER.equals(claims.getIssuer())) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN_ISSUER);
            }

            if (!claims.getAudience().contains(clientId)) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN_AUDIENCE);
            }

            Date expiration = claims.getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.EXPIRED_APPLE_ID_TOKEN);
            }

            // 7. DTO로 변환하여 반환
            return OAuthUserInfoResponseDto.builder()
                    .subject(claims.getSubject())
                    .email((String) claims.getClaim("email"))
                    .build();

        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.APPLE_PUBLIC_KEY_LOAD_ERROR);
        } catch (ParseException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TOKEN_PARSE_ERROR);
        } catch (JOSEException | BadJOSEException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TOKEN_PROCESSING_ERROR);
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorType.INVALID_TOKEN);
        }
    }
}