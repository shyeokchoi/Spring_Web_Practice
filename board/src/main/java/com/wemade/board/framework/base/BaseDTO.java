package com.wemade.board.framework.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDTO extends BaseSystemDTO {

    @Schema(description = "아이디")
    protected String id;

}
