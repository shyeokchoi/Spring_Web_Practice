package com.board.dto.file;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InsFileInfoDTO {
    private Integer no;
    private String originName;
    private String saveName;
    private String savePath;
    private String extension;
    private Long size;
    private Integer parentNo;
    private Integer authorNo;
}
