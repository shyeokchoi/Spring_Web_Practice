package com.board.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 로그인 요청을 위한 DTO
 */
@Data
@Schema(description = "로그인 요청 DTO")
public class SigninRequestDTO {
    @Size(max = 20, message = "아이디는 20자 이하여야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Schema(description = "회원 아이디", example = "hongildong")
    private String id;

    @Size(max = 20, message = "비밀번호는 100자 이하여야 합니다.")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Schema(description = "비밀번호", example = "examplepw1234!")
    private String pw;
}
