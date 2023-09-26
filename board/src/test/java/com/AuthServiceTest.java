package com;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.board.dto.member.IdPwDTO;
import com.board.exception.AuthenticationException;
import com.board.exception.NotSignedInException;
import com.board.mapper.auth.AuthMapper;
import com.board.service.auth.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private AuthMapper authMapper;

    @Test
    @DisplayName("Access Token이 null 일 경우")
    public void nullAccessToken() {
        // given
        String accessToken = null;

        // then
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authService.checkAccessTokenValidity(accessToken);
        });
    }

    @Test
    @DisplayName("Access Token이 비어있는 경우")
    public void emptyAccessToken() {
        // given
        String accessToken = "";

        // then
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authService.checkAccessTokenValidity(accessToken);
        });
    }

    @Test
    @DisplayName("Access Token이 공백일 경우")
    public void blankAccessToken() {
        // given
        String accessToken = "  ";

        // then
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authService.checkAccessTokenValidity(accessToken);
        });
    }

    @Test
    @DisplayName("해당 access token에 대응되는 멤버가 없는 경우")
    public void memberNotFound() {
        // given
        String accessToken = "some access token";

        // stub
        when(authMapper.selectMemberNoByAccessToken(anyString())).thenThrow(new NotSignedInException(
                "로그인하지 않았거나 이미 로그아웃한 토큰입니다."));

        // then
        Assertions.assertThrows(NotSignedInException.class, () -> {
            authService.checkAccessTokenValidity(accessToken);
        });
    }

    @Test
    @DisplayName("해당 access token에 대응되는 멤버가 있는 경우")
    public void memberFound() {
        // given
        String accessToken = "some access token";

        // stub
        when(authMapper.selectMemberNoByAccessToken(accessToken)).thenReturn(1);

        // when
        Integer memNo = authService.checkAccessTokenValidity(accessToken);

        // then
        Assertions.assertEquals(1, memNo);

    }

    @Test
    @DisplayName("아이디, 비밀번호가 틀린 경우")
    public void idPwMismatch() {
        // given
        IdPwDTO idPwDTO = new IdPwDTO();
        idPwDTO.setId("some id");
        idPwDTO.setPw("wrong pw");

        // stub
        when(authMapper.selectMemberNoByIdPW(idPwDTO)).thenReturn(null);

        // then
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authService.checkIdPw(idPwDTO);
        });
    }

    @Test
    @DisplayName("아이디, 비밀번호가 맞는 경우")
    public void idPwMatch() {
        // given
        IdPwDTO idPwDTO = new IdPwDTO();
        idPwDTO.setId("some id");
        idPwDTO.setPw("right pw");

        // stub
        when(authMapper.selectMemberNoByIdPW(idPwDTO)).thenReturn(1);

        // when
        Integer memNo = authService.checkIdPw(idPwDTO);

        // then
        Assertions.assertEquals(1, memNo);
    }
}
