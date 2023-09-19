package com.board.service.storage;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.board.enums.FileInfoParentTypeEnum;

public interface StorageService {

    /**
     * file info 새로 삽입
     * 
     * @param file
     * @param memberNo 파일 생성한 member no.
     */
    public Integer insFile(MultipartFile file, Integer memberNo) throws Exception;

    /**
     * 파일을 Resource로 반환
     * 
     * @param filename
     * @return
     */
    public Resource loadAsResource(Integer fileInfoNo) throws Exception;

    /**
     * file info 삭제
     * 
     * @param fileInfoNo
     * @throws Exception
     */
    public void deleteFile(Integer fileInfoNo) throws Exception;

    /**
     * file info 상태 변경. TEMP에서 NORMAL로
     * 
     * @param fileInfoNo 변경 대상이 될 파일의 no.
     * @param fileName   바뀔 파일 이름
     * @param parentNo   해당 파일에 설정해줄 parentNo
     * @param parentType 해당 파일에 설정해줄 parentType (게시글인지, 댓글인지 등등)
     */
    public void changeFileStatus(Integer fileInfoNo, String fileName, Integer parentNo,
            FileInfoParentTypeEnum parentType);

    /**
     * file의 이름 가져오기
     * 
     * @param fileInfoNo
     * @return
     */
    public String selectFileSaveName(Integer fileInfoNo);

    /**
     * file src에서 dst로 옮기기
     * 
     * @param src
     * @param dst
     * @param fileName
     * @throws IOException
     */
    public void moveFile(String src, String dst, String fileName) throws IOException;
}
