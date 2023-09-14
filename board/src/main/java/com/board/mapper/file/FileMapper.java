package com.board.mapper.file;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.file.InsFileInfoDTO;

@Mapper
public interface FileMapper {

    void insFileInfo(InsFileInfoDTO insFileInfoDTO);

}
