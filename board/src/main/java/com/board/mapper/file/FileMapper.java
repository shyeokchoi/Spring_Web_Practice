package com.board.mapper.file;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.file.FileInfoDTO;
import com.board.dto.file.InsFileInfoDTO;
import com.board.enums.FileInfoParentTypeEnum;

@Mapper
public interface FileMapper {
    public FileInfoDTO selectOne(Integer fileInfoNo);

    public void insFileInfo(InsFileInfoDTO insFileInfoDTO);

    public void changeFileStatus(
            @Param("fileInfoNo") Integer fileInfoNo,
            @Param("newPath") String newPath,
            @Param("parentNo") Integer parentNo,
            @Param("fileInfoParentType") FileInfoParentTypeEnum fileInfoParentType);

    public void deleteFileInfo(Integer fileInfoNo);
}
