package com.example.gitryeokoffice.global.config;

import com.example.gitryeokoffice.global.auth.JwtAuthenticationInterceptor;
import com.example.gitryeokoffice.global.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC 설정
 * - Interceptor 등록 (JWT 인증)
 * - ArgumentResolver 등록 (@LoginUser 어노테이션 지원)
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/api/**") // 모든 API 경로에 적용
                .excludePathPatterns(
                        "/api/users/signup",  // 회원가입은 인증 불필요
                        "/api/users/login",   // 로그인은 인증 불필요
                        "/api/users/logout",  // 로그아웃은 인증 불필요
                        "/api/v1/dev-vibe/**" // Dev-Vibe 분석 엔드포인트는 인증 불필요
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
