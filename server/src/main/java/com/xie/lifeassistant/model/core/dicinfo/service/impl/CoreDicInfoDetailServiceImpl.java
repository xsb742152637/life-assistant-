package com.xie.lifeassistant.model.core.dicinfo.service.impl;

import com.xie.lifeassistant.model.core.dicinfo.dao.CoreDicInfoDetailDao;
import com.xie.lifeassistant.model.core.dicinfo.entity.CoreDicInfoDetailEntity;
import com.xie.lifeassistant.model.core.dicinfo.service.CoreDicInfoDetailService;
import com.xie.lifeassistant.util.datamanage.GenericServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @comment 数据字典明细表
 * @author xie
 * @since 2020-09-28
 */
@Service
public class CoreDicInfoDetailServiceImpl extends GenericServiceImpl<CoreDicInfoDetailDao, CoreDicInfoDetailEntity> implements CoreDicInfoDetailService {

    @Override
    public void deleteByDicId(String dicId){
        getBaseMapper().deleteByDicId(dicId);
    }

}
