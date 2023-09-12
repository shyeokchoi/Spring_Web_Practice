package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * [GET] members/self 로 조회했을 때 멤버 정보를 반환하기 위한 DTO.
 */
@Getter
@Setter
public class SelectMemberDetailDTO {
    @Schema(description = "회원 번호")
    private Integer no;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 아이디")
    private String id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;
}
