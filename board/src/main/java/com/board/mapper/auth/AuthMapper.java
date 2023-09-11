package com.board.mapper.auth;

public interface AuthMapper {
    /**
     * 멤버 로그아웃 시 해당 Access Token에 해당하는 member no 반환
     * 
     * @return 해당 멤버의 no.
     */
    public Integer selectMemberNoByAccessToken(String accessToken);
}
