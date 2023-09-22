package com.board.dto.comment;

import lombok.Data;

@Data
public class CommentDTO {
    // PK
    private int no;

    // 댓글이 달린 게시물
    private int postNo;

    // 생성 시점
    private long createdAt;

    // 삭제 시점
    private Long deletedAt;

    // 작성자 no.
    private int author_no;

    // 내용
    private String content;

    // 수정 시점
    private Long modifiedAt;

    // 수정자 no.
    private Integer modifierNo;

    // 상태 (POSTED, DELETED)
    private String status;
}
