package com.board.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagingRequestWithSearchKeywordDTO extends PagingRequestDTO {
    private String searchKeyword;
}
