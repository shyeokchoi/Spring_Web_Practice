package com.board.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class InsMemberDTO {
    @JsonIgnore
    private Integer no;
    private String name;
    private String id;
    private String pw;
    private String email;
    private String phone;
}
