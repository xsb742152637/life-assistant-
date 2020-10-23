package com.xie.lifeassistant.model.core.dicinfo.service;

import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoDetailEntity;
import com.xie.lifeassistant.util.datamanage.GenericService;

/**
 * @comment 数据字典明细表
 * @author xie
 * @since 2020-09-28
 */
public interface CoreDicInfoDetailService extends GenericService<CoreDicInfoDetailEntity> {

    void deleteByDicId(String dicId);
}
