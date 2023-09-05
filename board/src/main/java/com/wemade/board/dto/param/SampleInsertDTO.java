package com.wemade.board.dto.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * apk 파일 등록을 위한 param 클래스 
 * 
 * @author : dev_tony85
 * @Date : 2023. 8. 10.
 * @Description :
 */
@Getter
@Setter
@ToString
public class SampleInsertDTO {

    @NotBlank
    @Schema(description = "게임 아이디")
    @JsonProperty(value="gId")
    private String gId;

    @NotBlank
    @Pattern(regexp = "\\d{1,3}.\\d{1,3}.\\d{1,3}")
    @Schema(description = "apk 버전", example = "**.**.**")
    private String version; ;
    
}
