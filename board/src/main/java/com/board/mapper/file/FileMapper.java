package com.board.mapper.file;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.file.InsFileInfoDTO;
import com.board.enums.FileInfoParentTypeEnum;

@Mapper
public interface FileMapper {
    /**
     * file info를 db에 저장합니다.
     * 
     * @param insFileInfoDTO
     */
    public void insFileInfo(InsFileInfoDTO insFileInfoDTO);

    /**
     * 파일의 상태를 변경합니다. TEMP 파일에서 NORMAL 파일이 됩니다.
     * 
     * @param fileInfoNo         상태를 변경할 file_info의 no.
     * @param parentNo           설정해줄 parent no.
     * @param fileInfoParentType 설정해줄 parent type
     */
    public void changeFileStatus(
            @Param("fileInfoNo") Integer fileInfoNo,
            @Param("newPath") String newPath,
            @Param("parentNo") Integer parentNo,
            @Param("fileInfoParentType") FileInfoParentTypeEnum fileInfoParentType);

    /**
     * 파일 save name (Random UUID + extension) 뽑아오기
     * 
     * @param fileInfoNo
     * @return
     */
    public String selectFileSaveName(Integer fileInfoNo);

    /**
     * 파일 save path 뽑아오기
     * 
     * @param fileInfoNo
     * @return
     */
    public String selectFileSavePath(Integer fileInfoNo);

    /**
     * 파일 info 삭제
     * 
     * @param fileInfoNo
     */
    public void deleteFileInfo(Integer fileInfoNo);
}
