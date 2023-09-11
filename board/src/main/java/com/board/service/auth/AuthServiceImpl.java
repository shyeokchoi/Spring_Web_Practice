package com.board.service.auth;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.board.exception.AlreadySignedOutException;
import com.board.exception.AuthenticationException;
import com.board.mapper.auth.AuthMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;

    @Override
    public String retvAccessTokenFromRequest(HttpServletRequest request) {

        return request.getHeader("Access-Token");
    }

    @Override
    public Integer retvMemberNoFromAccessToken(String accessToken) {
        if (accessToken == null) {
            throw new AuthenticationException("Access-Token 값이 null 입니다.");
        }
        // 로그아웃하려고 하는 멤버의 member_no 얻기
        Optional<Integer> memberNoOptional = Optional
                .ofNullable(authMapper.selectMemberNoByAccessToken(accessToken));

        // 만약 로그인 하지 않은 access token이거나 이미 로그아웃 되어 있다면 예외 처리
        if (!memberNoOptional.isPresent()) {
            throw new AlreadySignedOutException("로그인하지 않았거나 이미 로그아웃한 토큰입니다.");
        }

        return memberNoOptional.get();
    }

}
