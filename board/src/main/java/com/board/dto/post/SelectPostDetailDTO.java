package com.board.dto.post;

import lombok.Data;

/**
 * [GET] posts/{no} 를 호출했을 때 게시글의 상세정보를 반환하기 위한 DTO
 */
@Data
public class SelectPostDetailDTO {
    private String title;
    private String content;
    private String author;
    private Long createdAt;
    private Long modifiedAt;
}
