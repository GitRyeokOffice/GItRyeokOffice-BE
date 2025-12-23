package com.example.gitryeokoffice.user.exception;

import com.example.gitryeokoffice.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * User 도메인 에러 코드
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ExceptionCode {

    // 인증 관련
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 실패", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰", "만료된 토큰입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 누락", "토큰이 필요합니다."),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 조회 실패", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이메일 중복", "이미 사용 중인 이메일입니다."),
    DUPLICATE_GITHUB_LOGIN(HttpStatus.CONFLICT, "GitHub 아이디 중복", "이미 사용 중인 GitHub 아이디입니다."),
    DUPLICATE_DISPLAY_NAME(HttpStatus.CONFLICT, "닉네임 중복", "이미 사용 중인 닉네임입니다."),

    // 비즈니스 로직 관련
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 비밀번호 형식", "비밀번호는 8자 이상이어야 합니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;


    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
