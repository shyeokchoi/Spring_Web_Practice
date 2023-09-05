package com.wemade.board.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemade.board.framework.base.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SampleDTO extends BaseDTO {

    @Schema(description = "게임 아이디")
    @JsonProperty("gId")
	private String gId; 

    @Schema(description = "apk 버전")
	private String version; 

    @Schema(description = "apk Id")
    private String apkId;
    
    @Schema(description = "apk url")
	private String apkUrl;
    
    @Schema(description = "시직일시")
    private long startDate;

    @Schema(description = "종료일시")
    private long endDate;

    @Schema(description = "다운로드 카운트")
	private int dwCnt;

}