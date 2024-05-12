package com.board.dto.auth;

import lombok.Data;

@Data
public class MemberInfoDTO {
    private final String accessToken;
    private final Integer memberNo;
}
