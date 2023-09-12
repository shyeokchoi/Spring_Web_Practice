package com.board.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.board.service.auth.AuthService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SigninInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String accessToken = authService.retvAccessTokenFromRequest(request);
        authService.checkAccessTokenValidity(accessToken);

        return true;
    }
}
