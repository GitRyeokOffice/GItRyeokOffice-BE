package com.example.gitryeokoffice.user.presentation.dto;

import com.example.gitryeokoffice.user.domain.Position;
import com.example.gitryeokoffice.user.domain.RoleType;
import com.example.gitryeokoffice.user.domain.User;

/**
 * 사용자 정보 응답 DTO
 */
public record UserResponse(
        Long id,
        String githubLogin,
        String email,
        String displayName,
        RoleType roleType,
        Position position,
        String organization,
        Integer projectExperienceCount,
        Boolean isSprout,
        String profileImageUrl,
        String bio,
        String githubUrl
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getGithubLogin(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRoleType(),
                user.getPosition(),
                user.getOrganization(),
                user.getProjectExperienceCount(),
                user.getIsSprout(),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getGithubUrl()
        );
    }
}
