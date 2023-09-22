package com.board.dto.auth;

import lombok.Data;

@Data
public class MemberInfoDTO {
    private final String accessToken;
    private final int memberNo;
    private final String userAgent;
    private final String ipAddr;
}
