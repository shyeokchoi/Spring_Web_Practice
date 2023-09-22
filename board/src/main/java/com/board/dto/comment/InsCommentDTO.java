package com.board.dto.comment;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InsCommentDTO {
    @JsonIgnore
    private int no;

    @Min(1)
    private int postNo;

    @Size(min = 0, max = 300, message = "댓글은 300자 이하입니다")
    @NotBlank(message = "댓글은 공백일 수 없습니다")
    @Schema(description = "댓글 내용", example = "예시 댓글 내용")
    private String content;

    @JsonIgnore
    private int authorNo;
}
