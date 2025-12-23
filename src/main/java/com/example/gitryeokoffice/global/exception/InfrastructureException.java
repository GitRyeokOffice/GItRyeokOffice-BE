package com.example.gitryeokoffice.global.exception;

import lombok.Getter;

@Getter
public class InfrastructureException extends RuntimeException {

    private final ExceptionCode code;

    public InfrastructureException(ExceptionCode code) {
        super(code.getDetail());
        this.code = code;
    }
}
