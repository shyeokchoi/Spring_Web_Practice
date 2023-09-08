package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 반환 DTO")
public class SigninResponseDTO {
    @Schema(description = "Access Token")
    private String accessToken;
}
