package com.board.dto.member;

import com.board.enums.HistoryEnum;

import lombok.Builder;
import lombok.Getter;

/**
 * 멤버 히스토리를 받아오기 위한 DTO
 */
@Getter
@Builder
public class MemberHistoryDTO {
    /**
     * PK
     */
    private final Integer no;
    /**
     * 히스토리 생성 시점
     */
    private final Long timestamp;
    /**
     * 해당 히스토리 생성한 유저
     */
    private final Integer memberNo;
    /**
     * 로그인인지 로그아웃인지
     */
    private final HistoryEnum type;
}
