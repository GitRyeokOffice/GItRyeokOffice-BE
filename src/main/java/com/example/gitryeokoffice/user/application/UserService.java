package com.example.gitryeokoffice.user.application;

import com.example.gitryeokoffice.global.util.JwtTokenProvider;
import com.example.gitryeokoffice.user.domain.User;
import com.example.gitryeokoffice.user.domain.UserRepository;
import com.example.gitryeokoffice.user.exception.UserErrorCode;
import com.example.gitryeokoffice.user.exception.UserException;
import com.example.gitryeokoffice.user.presentation.dto.LoginRequest;
import com.example.gitryeokoffice.user.presentation.dto.SignupRequest;
import com.example.gitryeokoffice.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User 도메인 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse signup(SignupRequest request) {
        // GitHub 사용자명 중복 체크
        if (userRepository.existsByGithubLogin(request.githubLogin())) {
            throw new UserException(UserErrorCode.DUPLICATE_GITHUB_LOGIN);
        }

        // 닉네임 중복 체크
        if (request.displayName() != null && userRepository.existsByDisplayName(request.displayName())) {
            throw new UserException(UserErrorCode.DUPLICATE_DISPLAY_NAME);
        }

        // User 엔티티 생성 및 저장
        User user = User.create(
                request.githubLogin(),
                request.email(),
                request.password(), // TODO: 비밀번호 암호화 필요 (BCrypt 등)
                request.displayName(),
                request.roleType(),
                request.position(),
                request.organization(),
                request.projectExperienceCount(),
                request.isSprout()
        );

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료. userId: {}, displayName: {}", savedUser.getId(), savedUser.getDisplayName());

        return UserResponse.from(savedUser);
    }

    /**
     * 로그인
     */
    public String login(LoginRequest request) {
        // 닉네임으로 사용자 조회
        User user = userRepository.findByDisplayName(request.nickname())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증 (TODO: BCrypt 사용)
        if (!user.getPassword().equals(request.password())) {
            throw new UserException(UserErrorCode.INVALID_CREDENTIALS);
        }

        // JWT 토큰 생성 및 반환
        String token = jwtTokenProvider.createToken(user.getId());
        log.info("로그인 성공. userId: {}, displayName: {}", user.getId(), user.getDisplayName());

        return token;
    }

    /**
     * 사용자 정보 조회
     */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }
}
