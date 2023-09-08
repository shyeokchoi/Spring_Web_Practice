package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 로그인 성공 시 토큰을 반환해주기 위한 DTO
 */
@Data
@AllArgsConstructor
@Schema(description = "로그인 반환 DTO")
public class SigninResponseDTO {
    @Schema(description = "Access Token")
    private String accessToken;
}
