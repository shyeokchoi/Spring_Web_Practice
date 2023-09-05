package com.board.dto.post;

import lombok.Data;

@Data
public class PostDTO {
    private Integer no;
    private String title;
    private String content;
    private String author;
    private Long createdAt;
    private String modifier;
    private String modifiedAt;
    private String fileUrl;
    private Long deletedAt;
}
