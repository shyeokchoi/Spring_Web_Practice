package com.board.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 페이징과 검색에 사용될 파라미터들을 보관하는 DTO
 */
@Data
@AllArgsConstructor
public class PagingRequestDTO {
    private long currPage = 1; // 기본값 1
    private long pageSize = 10; // 기본값 10

    /**
     * 검색어. 작성자 이름과 제목을 동시에 검색한다
     */
    private String searchKeyword;

    /**
     * paging의 offset 계산해서 리턴
     * 
     * @return
     */
    @JsonIgnore
    public long getOffset() {
        return (currPage - 1) * pageSize;
    }

}
