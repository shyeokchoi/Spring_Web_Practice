package com.wemade.board.framework.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseSystemDTO {

    @Schema(description = "생성 일시")
    protected Long cretDt;

    @Schema(description = "생성자 아이디")
    protected String cretId;

    @Schema(description = "수정 일시")
    protected Long amdDt;

    @Schema(description = "수정자 아이디")
    protected String amdId;
}
