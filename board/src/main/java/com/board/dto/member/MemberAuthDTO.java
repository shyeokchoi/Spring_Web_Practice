package com.board.dto.member;

import com.board.enums.MemberAuthStatusEnum;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberAuthDTO {
    private final Integer no;

    private final Integer memberNo;

    private final String accessToken;

    private final String ipAddr;

    private final String userAgent;

    private final MemberAuthStatusEnum status;
}
