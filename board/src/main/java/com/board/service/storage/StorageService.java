package com.board.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * 파일을 저장할 디렉토리가 없는 경우 초기 디렉토리 설정
     */
    public void initDirectory();

    /**
     * 파일 저장
     * 
     * @param parentNo     파일이 저장될 글 no.
     * @param currMemberNo 글 작성자 no.
     * @param file         파일
     */
    @Transactional
    public void store(Integer parentNo, Integer currMemberNo, MultipartFile file);

    /**
     * 파일을 Resource로 반환
     * 
     * @param filename
     * @return
     */
    @Transactional
    public Resource loadAsResource(String filename);
}
