package com.wemade.board.framework.base;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wemade.board.common.DTO.Direction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class BasePagingParam {
    @Schema(description = "현재 페이지 [default: 1, min: 1]")
    @Min(1)
    private long currPage = 1;

    @Schema(description = "페이지 사이즈 [default: 20, min: 2, max: 200]")
    @Min(2)
    @Max(200)
    private long pageSize = 20;
    
    @Schema(description = "리스트 정렬 방향")
    private Direction direction;
    
    @Schema(description = "검색어")
    private String searchText;
    
    @Schema(description = "검색 타입", example = "title, writer")
    private String searchType;
    
    /**
     * paging의 offset 계산해서 리턴 
     * @return
     */
    @JsonIgnore
    public long getOffset() {
        return (currPage -1) * pageSize;
    }
    
    
    
}
