package com.example.gitryeokoffice.devvibe.exception;

import com.example.gitryeokoffice.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DevVibeErrorCode implements ExceptionCode {
    GITHUB_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "GitHub 사용자 조회 실패", "해당하는 GitHub 사용자를 찾을 수 없습니다."),
    GITHUB_API_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GitHub API 연동 실패", "GitHub API 연동 중 오류가 발생했습니다.");

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
