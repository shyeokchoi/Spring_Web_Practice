package com.board.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostSimpleDTO {
    @Schema(description = "게시물의 Primary Key")
    private Integer no;

    @Schema(description = "게시물의 제목")
    private String title;

    @Schema(description = "게시물 작성자 이름")
    private String authorName;

    @Schema(description = "게시물 작성 시각")
    private Long createdAt;
}
