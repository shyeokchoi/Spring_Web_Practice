package com.board.dto.post;

import lombok.Data;

@Data
public class SelectPostListDTO {
    private Integer no;
    private String title;
    private String author;
    private Long createdAt;
}
