package com.board.dto.comment;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer no;
    private String author;
    private String content;
    private Integer postNo;
    private Long createdAt;
    private Long deletedAt;
}
