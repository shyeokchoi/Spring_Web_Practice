package com.board.mapper.file;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.file.InsFileInfoDTO;
import com.board.enums.FileInfoParentTypeEnum;

@Mapper
public interface FileMapper {
    /**
     * file info를 db에 저장합니다.
     * 
     * @param insFileInfoDTO
     */
    void insFileInfo(InsFileInfoDTO insFileInfoDTO);

    /**
     * 파일의 상태를 변경합니다. TEMP 파일에서 NORMAL 파일이 됩니다.
     * 
     * @param fileNo             상태를 변경할 file_info의 no.
     * @param parentNo           설정해줄 parent no.
     * @param fileInfoParentType 설정해줄 parent type
     */
    void changeFileStatus(Integer fileNo, Integer parentNo, FileInfoParentTypeEnum fileInfoParentType);

}
