package com.board.dto.common;

import lombok.Data;

@Data
public class PagingDTO {
    private final Integer offset;
    private final Integer limit;

}
