package com.board.service.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.board.common.ResourceAndOriginName;
import com.board.enums.FileInfoParentTypeEnum;

public interface StorageService {
    public Integer insFile(MultipartFile file, int memberNo);

    public ResourceAndOriginName loadAsResource(int fileInfoNo);

    public void deleteFileInfo(int fileInfoNo);

    public void deleteFile(int fileInfoNo);

    public void changeFileStatus(int fileInfoNo, String fileName, int parentNo,
            FileInfoParentTypeEnum parentType);

    public void moveFile(String src, String dst, String fileName) throws IOException;

    public void recursiveDelete(String target) throws IOException;
}
