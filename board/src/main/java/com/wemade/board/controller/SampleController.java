/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Mon Jun 19 2023
 * Description :
 *
 ****************************************************/
package com.wemade.board.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wemade.board.common.DTO.InsertUpdateResultDTO;
import com.wemade.board.common.DTO.PagingData;
import com.wemade.board.common.DTO.ResponseDTO;
import com.wemade.board.common.constant.FrkConstants;
import com.wemade.board.dto.SampleDTO;
import com.wemade.board.dto.param.SampleInsertDTO;
import com.wemade.board.dto.param.SampleUpdateParam;
import com.wemade.board.framework.base.BaseController;
import com.wemade.board.framework.base.BasePagingParam;
import com.wemade.board.framework.exception.BaseException;
import com.wemade.board.service.SampleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Sample", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/sample")
public class SampleController extends BaseController {
    
    private final SampleService sampleService;

    
    @Tag(name = "Sample", description = "")
    @Operation(summary = "DB의 현재 날짜 조회", description = "")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @GetMapping("/retvNow")
    public ResponseEntity<ResponseDTO<String>> retvNow() {
        String now = sampleService.retvNow();
        return ok(now);
    } 
    
    @Tag(name = "Sample", description = "")
    @Operation(
            summary = "apk 파일 등록", description = "apk 파일 등록.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(encoding = @Encoding(name="param", contentType=MediaType.APPLICATION_JSON_VALUE))
            )
        )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공")})
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<InsertUpdateResultDTO>> insApkFile(@Valid @RequestPart(value="param") SampleInsertDTO param
            , @Schema(description = "apk 파일") @RequestPart(value="apkFile") MultipartFile apkFile
            ) {
        // 파일명이 .apk로 끝나지 않으면 에러 처리
        if(!apkFile.getOriginalFilename().endsWith(".apk")) {
            throw new BaseException("file is not apkFile. *fileName : " + apkFile.getOriginalFilename(), FrkConstants.CD_PARAM_ERR);
        }
        
        String apkId = sampleService.insSample(param, apkFile);
        return ok(new InsertUpdateResultDTO(apkId));
    }

    @Tag(name = "Sample", description = "")
    @Operation(summary = "현재의 apk 버전 조회", description = "현재의 apk 버전 조회")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @GetMapping("/retvGameApk/{gId}")
    public ResponseEntity<ResponseDTO<SampleDTO>> retvGameApk(@PathVariable(required = true) @Schema(description = "아이디") String id) {
        SampleDTO sample = sampleService.retvSample(id);
        return ok(sample);
    } 
    
    @Tag(name = "Sample", description = "")
    @Operation(summary = "apk 리스트 조회 (페이징)", description = "api에 대한 설명을  자세한 설명을 넣습니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @PostMapping("/retvGameApkInfoListByPaging")
    public ResponseEntity<ResponseDTO<PagingData<SampleDTO>>> retvGameApkInfoListByPaging(@Valid @RequestParam BasePagingParam param) {
        PagingData<SampleDTO> pagingData = sampleService.retvSampleListByPaging(param);
        return ok(pagingData);
    }
    
    @Tag(name = "Sample", description = "")
    @Operation(
            summary = "토큰 기본정보 수정", description = "토큰 기본정보 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(encoding = @Encoding(name="param", contentType=MediaType.APPLICATION_JSON_VALUE))
            )
        )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @PostMapping(value = "/udateToken", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<InsertUpdateResultDTO>> updateToken(@Valid @RequestPart(value="param") SampleUpdateParam updateParam
            ,@RequestPart(value="symbolFile") MultipartFile symbolFile
            ) {
       
        String tId = sampleService.updateSample(updateParam, symbolFile);
        return ok(new InsertUpdateResultDTO(tId));
    }

}