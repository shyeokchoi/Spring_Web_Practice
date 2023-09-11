package com.board.service.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    /**
     * HTTP servlet request 에서 access token을 얻습니다.
     * 
     * @param request
     * @return
     */
    public String retvAccessTokenFromRequest(HttpServletRequest request);

    /**
     * Access token을 이용해 member no.를 얻습니다.
     * 
     * @param accessToken
     * @return
     */
    public Integer retvMemberNoFromAccessToken(String accessToken);
}
