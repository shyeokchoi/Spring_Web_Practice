package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 회원 정보를 받아오기 위한 DTO
 */
@Data
@Schema(description = "회원 정보")
public class MemberDTO {
    @Schema(description = "회원 번호")
    private Integer no;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 아이디")
    private String id;

    @Schema(description = "비밀번호")
    private String pw;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;

    @Schema(description = "탈퇴시간")
    private Long withdrawnAt;
}
