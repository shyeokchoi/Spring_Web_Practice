package com.board.dto.post;

import com.board.enums.PostStatusEnum;

import lombok.Data;

@Data
public class PostDTO {
    private Integer no;
    private String title;
    private String content;
    private Integer authorNo;
    private Long createdAt;
    private Integer modifierNo;
    private Long modifiedAt;
    private Long deletedAt;
    private PostStatusEnum status;
}
