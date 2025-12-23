package com.example.gitryeokoffice.user.domain;

/**
 * 사용자 역할 타입
 * GitHub 데이터 분석 가능 여부 구분
 */
public enum RoleType {
    DEVELOPER,      // 개발자 (GitHub 데이터 분석 대상)
    NON_DEVELOPER   // 비개발자 (디자이너, 기획자 등 - 자가 진단)
}
