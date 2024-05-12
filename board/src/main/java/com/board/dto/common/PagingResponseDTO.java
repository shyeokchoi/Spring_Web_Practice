package com.board.dto.common;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingResponseDTO<T> {
    @Schema(description = "결과 목록")
    private List<T> dataList;

    @Schema(description = "현재 페이지")
    private long currPage;

    @Schema(description = "페이지 사이즈")
    private long pageSize;

    @Schema(description = "전체 카운트")
    private long totalCount;
}
