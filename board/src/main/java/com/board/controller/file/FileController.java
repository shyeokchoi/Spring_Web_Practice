package com.board.controller.file;

import javax.validation.constraints.Min;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.board.common.ResourceAndOriginName;
import com.board.constant.RequestAttributeKeys;
import com.board.dto.auth.MemberInfoDTO;
import com.board.framework.base.BaseController;
import com.board.service.storage.StorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "files", description = "파일 관련 API")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController extends BaseController {
    private final StorageService storageService;

    @Operation(summary = "파일 등록")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> insFile(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody MultipartFile file) {
        return ok(storageService.insFile(file, memberInfoDTO.getMemberNo()));

    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/{fileInfoNo}")
    public ResponseEntity<Resource> downloadFile(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable(name = "fileInfoNo", required = true) @Min(1) int fileInfoNo) {
        ResourceAndOriginName fileAndName = storageService.loadAsResource(fileInfoNo);
        Resource file = fileAndName.getResource();
        String fileName = fileAndName.getOriginName();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(fileName, fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping("/{fileInfoNo}")
    public ResponseEntity<Void> deleteFileInfo(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable(name = "fileInfoNo", required = true) @Min(1) int fileInfoNo) {
        storageService.deleteFile(fileInfoNo);
        return ok();
    }
}
