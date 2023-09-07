package com.board.dto.member;

import lombok.Data;

@Data
public class MemberDTO {
    private Integer no;
    private String name;
    private String id;
    private String pw;
    private String email;
    private String phone;
    private Long withdrawnAt;
}
