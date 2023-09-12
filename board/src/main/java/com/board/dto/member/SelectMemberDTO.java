package com.board.dto.member;

import lombok.Data;

/**
 * 멤버를 검색하기 위한 DTO.
 * id, email로 검색할 수 있다.
 */
@Data
public class SelectMemberDTO {
    private Integer no;
    private String id;
    private String email;
}
