package com.board.dto.common;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequestDTO {
    @Min(1)
    private long currPage = 1;

    @Min(1)
    @Max(200)
    private long pageSize = 10;

    @JsonIgnore
    public long getOffset() {
        return (currPage - 1) * pageSize;
    }
}
