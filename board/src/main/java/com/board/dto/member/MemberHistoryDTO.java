package com.board.dto.member;

import com.board.enums.HistoryEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberHistoryDTO {
    private final Integer no;

    private final Integer memberNo;

    private final HistoryEnum type;
}
