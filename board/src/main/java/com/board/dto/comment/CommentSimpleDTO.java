package com.board.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectCommentListDTO {
    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 작성자 이름")
    private String authorName;

    @Schema(description = "댓글 작성 시각")
    private Long createdAt;
}
