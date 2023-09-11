package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 요청을 위한 DTO
 */

@Schema(description = "로그인 요청 DTO")
public class SigninRequestDTO extends IdPwDTO {
}
