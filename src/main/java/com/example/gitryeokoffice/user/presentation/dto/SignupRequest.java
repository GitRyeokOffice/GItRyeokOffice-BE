package com.example.gitryeokoffice.user.presentation.dto;

import com.example.gitryeokoffice.user.domain.Position;
import com.example.gitryeokoffice.user.domain.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 회원가입 요청 DTO
 */
public record SignupRequest(
        @NotBlank(message = "GitHub 사용자명은 필수입니다.")
        String githubLogin,

        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        String displayName,

        @NotNull(message = "역할 타입은 필수입니다.")
        RoleType roleType,

        Position position,

        String organization,

        Integer projectExperienceCount,

        Boolean isSprout
) {
}
