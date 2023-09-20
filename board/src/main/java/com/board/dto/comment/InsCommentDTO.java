package com.board.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.board.enums.CommentStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InsCommentDTO {
    @JsonIgnore
    private Integer no;

    @JsonIgnore
    private Integer postNo;

    @Size(min = 0, max = 300, message = "댓글은 300자 이하입니다")
    @NotBlank(message = "댓글은 공백일 수 없습니다")
    @Schema(description = "댓글 내용", example = "예시 댓글 내용")
    private String content;

    @JsonIgnore
    private Integer authorNo;

    @JsonIgnore
    private CommentStatusEnum status;
}
