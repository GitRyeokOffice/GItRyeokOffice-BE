package com.example.gitryeokoffice.user.exception;

import com.example.gitryeokoffice.global.exception.ApplicationException;
import com.example.gitryeokoffice.global.exception.ExceptionCode;

/**
 * User 도메인 예외
 */
public class UserException extends ApplicationException {

    public UserException(ExceptionCode code) {
        super(code);
    }
}
