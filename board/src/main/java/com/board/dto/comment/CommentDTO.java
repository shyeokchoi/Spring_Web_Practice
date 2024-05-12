package com.board.dto.comment;

import com.board.enums.CommentStatusEnum;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommentDTO {
    private Integer no;
    private Integer postNo;
    private Long createdAt;
    private Long deletedAt;
    private Integer authorNo;
    private String content;
    private Long modifiedAt;
    private Integer modifierNo;
    private CommentStatusEnum status;
}
