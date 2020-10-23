package com.xie.lifeassistant.model.core.dicinfo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoDetailEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * @comment 数据字典明细表
 * @author xie
 * @since 2020-09-28
 */

public interface CoreDicInfoDetailDao extends BaseMapper<CoreDicInfoDetailEntity> {

    @Delete("delete from core_dic_info_detail where dic_id = #{dicId}")
    void deleteByDicId(@Param("dicId") String dicId);

}
