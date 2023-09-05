package com.wemade.board.common.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PagingData<T> {

    @Schema(description = "결과 목록")
    private List<T> dataList;

    @Schema(description = "현재 페이지 [defalut: 1]")
    private long currPage = 1; // 현재 Page 번호

    @Schema(description = "페이지 사이즈 [default : 20]")
    private long pageSize = 20; // 페이지당 사이즈

    @Schema(description = "전체 카운트 [default: 0]")
    private long totalCount; // 전체카운트

}