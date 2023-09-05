package com.wemade.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wemade.board.dto.SampleDTO;
import com.wemade.board.dto.param.SampleUpdateParam;
import com.wemade.board.framework.base.BasePagingParam;

@Mapper
public interface SampleMapper {

    public String selectNow();
    
    public List<SampleDTO> selectListByPaging(BasePagingParam parm);

    public int selectTotalRow(BasePagingParam param);
    
    public SampleDTO selectOne(String id);

    public int updateSample(SampleUpdateParam param);
    
    

}

