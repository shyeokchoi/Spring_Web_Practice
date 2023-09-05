package com.board.dto.member;

import lombok.Data;

@Data
public class MemberDTO {
    private Integer no;
    private String name;
    private String id;
    private String email;
    private String phone;
    private Long lastLogin;
    private Long lastLogout;
    private String accessToken;
    private String refreshToken;
    private Long withdrawnAt;
}
