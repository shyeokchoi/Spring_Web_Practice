package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원탈퇴 요청을 위한 DTO
 */
@Getter
@Setter
@Schema(description = "회원탈퇴 요청 DTO")
public class WithdrawRequestDTO {
    @Schema(description = "Access Token")
    private String accessToken;
}
