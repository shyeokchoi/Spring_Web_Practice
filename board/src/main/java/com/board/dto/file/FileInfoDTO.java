package com.board.dto.file;

import com.board.enums.FileInfoParentTypeEnum;
import com.board.enums.FileStatusEnum;

import lombok.Data;

@Data
public class FileInfoDTO {
    private Integer no;

    private String originName;

    private String saveName;

    private String savePath;

    private String extension;

    private Long size;

    private Integer parentNo;

    private FileInfoParentTypeEnum parentType;

    private Integer authorNo;

    private FileStatusEnum status;

    private Integer parentCnt;
}
