package com.example.gitryeokoffice.devvibe.exception;

import com.example.gitryeokoffice.global.exception.ApplicationException;
import com.example.gitryeokoffice.global.exception.ExceptionCode;

public class DevVibeException extends ApplicationException {

    public DevVibeException(ExceptionCode code) {
        super(code);
    }
}
