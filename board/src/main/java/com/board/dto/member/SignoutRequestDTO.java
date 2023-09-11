package com.board.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SignoutRequestDTO {
    @Schema(description = "Access Token")
    private String accessToken;
}
