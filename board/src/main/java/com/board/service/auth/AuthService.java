package com.board.service.auth;

import com.board.dto.member.IdPwDTO;

/**
 * Access Token을 관리하고 id, pw 일치여부를 확인하는 등
 * 인증 전반을 담당하는 클래스.
 */
public interface AuthService {
    /**
     * HTTP servlet request 에서 access token을 반환합니다.
     * 
     * @return access token
     */
    public String retvAccessTokenFromHeader();

    /**
     * Access token의 validity를 확인하고 valid하다면 member no.를 반환합니다.
     * 
     * @param accessToken
     * @return
     */
    public Integer checkAccessTokenValidity(String accessToken);

    /**
     * 아이디, 패스워드 매칭여부 확인
     * 
     * @param idPwDto
     * @return member no.
     */
    public Integer checkIdPw(IdPwDTO idPwDto);
}
