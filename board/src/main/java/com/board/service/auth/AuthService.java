package com.board.service.auth;

import com.board.dto.member.IdPwDTO;


public interface AuthService {
    public Integer checkAccessTokenValidity(String accessToken);

    public Integer checkIdPw(IdPwDTO idPwDto);
}
