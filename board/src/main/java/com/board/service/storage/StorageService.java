package com.board.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * 파일을 저장할 디렉토리가 없는 경우 초기 디렉토리 설정
     */
    public void initDirectory();

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
}
