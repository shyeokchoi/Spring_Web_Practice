package com.board.dto.post;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * [GET] posts/{no} 를 호출했을 때 게시글의 상세정보를 반환하기 위한 DTO
 */
@Data
public class SelectPostDetailDTO {
    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "게시글 작성자 이름")
    private String authorName;

    @Schema(description = "게시글 수정자 이름")
    private String modifierName;

    @Schema(description = "게시글 작성시각")
    private Long createdAt;

    @Schema(description = "게시글 수정시각")
    private Long modifiedAt;

    @Schema(description = "해당 게시글에 등록된 파일들의 no.")
    private List<Integer> fileNoList;
}
