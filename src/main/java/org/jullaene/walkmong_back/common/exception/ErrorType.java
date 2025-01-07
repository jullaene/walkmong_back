package org.jullaene.walkmong_back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    ACCESS_DENIED("접근 권한이 없습니다."),
    USER_NOT_AUTHENTICATED("인증되지 않은 유저입니다."),
    INVALID_USER("존재하지 않는 유저입니다."),
    LOGIN_REQUIRED("로그인 해주세요."),
    EXPIRED_TOKEN("만료된 요청입니다."),
    ALREADY_EXIST_USER("이미 존재하는 유저입니다."),
    ALREADY_EXIST_NICKNAME("이미 존재하는 닉네임입니다."),
    INVALID_VERIFICATION_CODE("인증에 실패했습니다."),
    ALREADY_LOGIN("로그인 상태입니다"),
    WRONG_PASSWORD("잘못된 비밀번호 입니다."),
    REQUEST_VALIDATION_ERROR("유효성 검사가 실패하였습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INTERNAL_SERVER("서버 오류입니다."),
    UNAUTHORIZED_UPDATE("수정 권한이 없는 유저입니다."),
    DOG_NOT_FOUND("존재하지 않는 강아지입니다."),
    INVALID_ADDRESS("유효하지 않은 주소입니다."),
    CANNOT_SELF_APPLY("본인의 게시글에는 지원할 수 없습니다."),
    CANNOT_DUPLICATED_APPLY("한 게시글에 여러 번 지원할 수 없습니다."),
    CANNOT_DUPLICATED_DOG_PROFILE("이미 등록된 강아지입니다"),
    INVALID_FILE("존재하지 않는 파일입니다."),
    INVALID_S3_FILE("AWS S3 저장소에 존재하지 않는 파일입니다."),
    APPLY_NOT_FOUND("존재하지 않는 지원입니다"),
    POST_NOT_FOUND("존재하지 않는 산책글입니다"),
    INVALID_REFRESH_TOKEN("유효하지 않은 refresh 토큰입니다."),
    DUPLICATED_BASIC_ADDRESS("기본 주소가 이미 존재합니다.");

    private String message;
}
