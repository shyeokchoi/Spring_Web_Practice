package com.wemade.board.dto.param;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 토큰정보 수정을 위한 파라미터
 * 
 * @author : dev_tony85
 * @Date : 2023. 8. 12.
 * @Description :
 */
@Getter
@Setter
@ToString
public class SampleUpdateParam {
    
    @Schema(description = "토큰 아이디")
    @JsonProperty("tId")
    private String tId; 

    @Schema(description = "백서")
    private String whitePaper; 

    @Schema(description = "scope url")
    private String scopeUrl;

}