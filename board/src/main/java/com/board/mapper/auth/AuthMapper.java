package com.board.mapper.auth;

import com.board.dto.member.IdPwDTO;

public interface AuthMapper {
    /**
     * 멤버 로그아웃 시 해당 Access Token에 해당하는 member no 반환
     * 
     * @return 해당 멤버의 no.
     */
    public Integer selectMemberNoByAccessToken(String accessToken);

    /**
     * 로그인
     * 
     * @param IdPwDTO
     * @return 로그인 성공여부
     */
    public Integer checkIdPw(IdPwDTO idPwDTO);
}
