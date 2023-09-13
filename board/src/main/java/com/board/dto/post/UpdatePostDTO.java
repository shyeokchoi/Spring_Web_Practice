package com.board.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePostDTO {
    @JsonIgnore
    private Integer postNo;

    @Size(min = 0, max = 150, message = "제목은 150글자 이하입니다.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Schema(description = "제목", example = "예시 제목")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Schema(description = "내용", example = "예시 내용")
    private String content;

    @JsonIgnore
    private Integer modifierNo;

}
