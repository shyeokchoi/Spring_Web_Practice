package com.board.service.auth;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.board.dto.member.IdPwDTO;
import com.board.exception.AuthenticationException;
import com.board.exception.NotSignedInException;
import com.board.mapper.auth.AuthMapper;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;
    private final PwEncryptor pwEncryptor;

    @Override
    public String retvAccessTokenFromHeader() {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        return servletRequest.getHeader("Access-Token");
    }

    @Override
    public Integer checkAccessTokenValidity(String accessToken) {
        if (accessToken == null) {
            throw new AuthenticationException("Access-Token 값이 null 입니다.");
        }
        // 로그아웃하려고 하는 멤버의 member_no 얻기
        Optional<Integer> memberNoOptional = Optional
                .ofNullable(authMapper.selectMemberNoByAccessToken(accessToken));

        // 만약 로그인 하지 않은 access token이거나 이미 로그아웃 되어 있다면 예외 처리
        if (!memberNoOptional.isPresent()) {
            throw new NotSignedInException("로그인하지 않았거나 이미 로그아웃한 토큰입니다.");
        }

        return memberNoOptional.get();
    }

    /**
     * 주어진 아이디, 패스워드 일치여부 확인
     * 
     * @param idPwDto
     * @return 일치하였다면 해당 멤버의 member no.
     */
    @Override
    public Integer checkIdPw(IdPwDTO idPwDto) {
        // 비밀번호 암호화
        idPwDto.setPw(pwEncryptor.encryptPw(idPwDto.getPw()));

        // 아이디-패스워드 일치여부 확인
        Optional<Integer> memberNoOptional = Optional.ofNullable(authMapper.checkIdPw(idPwDto));

        if (!memberNoOptional.isPresent()) {
            throw new AuthenticationException("아이디와 비밀번호를 확인해주세요.");
        }

        return memberNoOptional.get();
    }
}
