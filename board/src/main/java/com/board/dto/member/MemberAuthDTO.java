package com.board.dto.member;

import com.board.constant.enums.MemberAuthStatus;

import lombok.Data;

@Data
public class MemberAuthDTO {
    private Integer no;
    private Integer memberNo;
    private String accessToken;
    private String ipAddr;
    private String userAgent;
    private MemberAuthStatus status;
    private Long createdAt;
}
