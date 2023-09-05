/***************************************************
 * Copyright(c) 2023-2024 WEMADE right reserved.
 *
 * Revision History
 * Author : hubert
 * Date : Mon Jun 19 2023
 * Description :
 *
 ****************************************************/
package com.wemade.board.service;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wemade.board.common.DTO.PagingData;
import com.wemade.board.dto.SampleDTO;
import com.wemade.board.dto.param.SampleInsertDTO;
import com.wemade.board.dto.param.SampleUpdateParam;
import com.wemade.board.framework.base.BasePagingParam;
import com.wemade.board.mapper.SampleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {
    
    private final SampleMapper sampleMapper;

    public String retvNow() {
        return sampleMapper.selectNow();
    }

    /**
     * 샘플 조회
     * @param id
     * @return
     */
    public SampleDTO retvSample(String id) {
        return sampleMapper.selectOne(id);
    }

    /**
     * 등록 샘플
     * @param param
     * @param apkFile
     * @return
     */
    @Transactional
    public String insSample(@Valid SampleInsertDTO param, MultipartFile apkFile) {
//        // 게임 조회
//        GameBasInfoDTO gameInfo = gameBasInfoService.retvGameBasInfo(insertParam.getGId(), null);
//        if(gameInfo == null) {
//            return null;
//        }
//        
//        // 현재 버전 조회
//        String curVersionStr = insertParam.getVersion();
//        
//        // 마지막 apk파일의 버전 조회
//        GameApkInfoDTO lastApkFile = gameApkInfoService.retvGameApkInfo(insertParam.getGId());
//        
//        if(lastApkFile != null) {
//            String lastVersionStr = lastApkFile.getVersion();
//            
//            // 버전에 대한 유효성 체크. 마지막 apk파일보다 버전이 높아야 한다.
//            checkApkVersionValid(curVersionStr, lastVersionStr);
//        }
//        
//        // 파일 업로드
//        String fileDir = apkPath + "/" + insertParam.getGId();
//        CmAttachInfoDTO fileDTO = uploadFile(fileDir, null, apkFile);
//        
//        if(StringUtils.isBlank(fileDTO.getId()) || StringUtils.isBlank(fileDTO.getFileUrl())) {
//            throw new BaseException("fileDTO is not valid. fileDTO : " + fileDTO.toString(), FrkConstants.CD_INTERNAL_ERR);
//        }
//        
//        GameApkInfoDTO apkDTO = CommonUtil.copyProperties(insertParam, GameApkInfoDTO.class);
//        apkDTO.setApkId(fileDTO.getId());
//        
//        int insertCnt = gameApkInfoService.insGameApkInfo(apkDTO);
//        if(insertCnt != 1) {
//            return null;
//        }
//        
//        return apkDTO.getId();
        
        return null;
    }

    /**
     * 페이징 리스트 조회 샘플
     * @param param
     * @return
     */
    public PagingData<SampleDTO> retvSampleListByPaging(@Valid BasePagingParam param) {
//        if(param.getDirection() == null) param.setDirection(Direction.DESC);
//        
//        int totalRow = gameApkInfoMapper.selectTotalRow(param);
//        
//        if(totalRow > 0) {
//            PagingData<GameApkInfoDTO> pagingData = new PagingData<>();
//            pagingData.setTotalCount(totalRow);
//            pagingData.setCurrPage(param.getCurrPage());
//            pagingData.setPageSize(param.getPageSize());
//            pagingData.setDataList(gameApkInfoMapper.selectByPaging(param));
//            
//            return pagingData;
//        }
        return null;
    }

    /**
     * 업데이트 샘플
     * @param updateParam
     * @param symbolFile
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateSample(@Valid SampleUpdateParam updateParam, MultipartFile symbolFile) {
        // TODO Auto-generated method stub
        return null;
    }
    
    

}
