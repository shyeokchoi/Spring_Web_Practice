package com.board.dto.member;

import com.board.enums.MemberAuthStatusEnum;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberAuthDTO {
    /**
     * PK
     */
    private final Integer no;
    /**
     * member no.
     */
    private final Integer memberNo;
    /**
     * 해당 멤버의 access token
     */
    private final String accessToken;
    /**
     * 해당 멤버의 ip address
     */
    private final String ipAddr;
    /**
     * 해당 멤버의 userAgent
     */
    private final String userAgent;
    /**
     * 이 토큰의 현재 상태. Valid or Expired
     */
    private final MemberAuthStatusEnum status;
    /**
     * 이 토큰 생성 시점
     */
    private final Long createdAt;
}
