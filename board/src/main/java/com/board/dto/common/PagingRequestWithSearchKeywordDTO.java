package com.board.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagingRequestWithSearchKeywordDTO extends PagingRequestDTO {
    /**
     * 검색어. 작성자 이름과 제목을 동시에 검색한다
     */
    private String searchKeyword;
}
