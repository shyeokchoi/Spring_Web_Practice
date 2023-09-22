package com.board.dto.common;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 페이징과 검색에 사용될 파라미터들을 보관하는 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequestDTO {
    @Min(1)
    private long currPage = 1; // 기본값 1

    @Min(1)
    @Max(200)
    private long pageSize = 10; // 기본값 10

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
