/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : dev_tony85
 * Date : Mon Jun 19 2023
 * Description : ifc live count response VO
 *
 ****************************************************/
package com.wemade.board.common.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * base Response
 *
 * @author : dev_tony85
 * @Date : 2023. 7. 4.
 * @Description :
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BaseResponseDTO {

    @Schema(description = "result code")
    protected int result;

    @Schema(description = "result string")
    protected String resultString;

    public BaseResponseDTO(int result, String resultString) {
        this.result = result;
        this.resultString = resultString;
    }

}

