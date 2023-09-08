package com.board.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 DTO")
public class SigninRequestDTO {
    @Size(max = 20, message = "아이디는 20자 이하여야 합니다.")
    @NotBlank
    @Schema(description = "회원 아이디")
    private String id;

    @Size(max = 20, message = "비밀번호는 100자 이하여야 합니다.")
    @NotBlank
    @Schema(description = "비밀번호")
    private String pw;
}
